package com.pet001kambala.controller

import com.pet001kambala.model.BinTransaction
import com.pet001kambala.repo.BinTransactionRepo
import com.pet001kambala.utils.Results
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.stage.Modality
import javafx.stage.StageStyle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class CurrentTransactionTableController : AbstractModelTableController<BinTransaction>("") {

    lateinit var progress: ProgressIndicator
    override val root = vbox {
        scrollpane {
            tableview<BinTransaction> {
                prefHeight = 200.0
                prefWidth = 650.0
                minHeight = prefHeight
                minWidth = prefWidth

                items = modelList
                smartResize()

                column("Date", BinTransaction::dateProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 1.0, useAsMin = true)
                }
                column("Waybill", BinTransaction::waybillNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 1.0, useAsMin = true)
                }
                column("Fish", BinTransaction::fish).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 1.0, useAsMin = true)
                }
                column("Bin", BinTransaction::binNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 1.0, useAsMin = true)
                }
                column("Pit", BinTransaction::pitNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 2.0, useAsMin = true)
                }
                column("Bin weight (KG)", BinTransaction::binWeightProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 1.0, useAsMin = true)
                }
                column("Factory", BinTransaction::factory).apply {
                    contentWidth(padding = 1.0, useAsMin = true)
                    remainingWidth()
                }
                placeholder = Label("There are no bins on that waybill yet.")
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                vgrow = Priority.ALWAYS
            }
        }
        hbox(10.0) {
            paddingAll = 5.0
            id = "bin-search-by-waybill"
            styleClass.add("bin-search-by-waybill")
            val waybillProp = SimpleStringProperty()

            textfield {
                promptText = "Waybill number"
                bind(waybillProp)
                setOnMouseClicked { showKeyPad(waybillProp) }
            }
            button("Search") {
                graphic = FontAwesomeIconView(FontAwesomeIcon.SEARCH)
                isDisable = true

                waybillProp.addListener(ChangeListener { _, _, newValue ->
                    this@button.isDisable = newValue.isNullOrEmpty()
                })

                setOnAction {
                    GlobalScope.launch {

                        progress.show()
                        val results = BinTransactionRepo().loadCurrentTransactionBins(waybillProp.get())
                        progress.hide()

                        modelList.asyncItems {
                            if (results is Results.Success<*>)
                                results.data as ObservableList<BinTransaction>
                            else {
//                                parseResults(results)
                                observableListOf()
                            }
                        }
                    }
                }
            }
            button("Clear") {
                graphic = FontAwesomeIconView(FontAwesomeIcon.CLOSE)
                setOnAction {
                    modelList.asyncItems { observableListOf() }
                    waybillProp.set(null)
                }
            }
            progress = progressindicator {
                id = "search-bin-indicator"
                styleClass.add("search-bin-indicator")
                hide()
            }
        }
    }

    override suspend fun loadModels(): ObservableList<BinTransaction> {

        return observableListOf()
    }

    override fun onDock() {
        super.onDock()
        title = "Search bins"
        modalStage?.scene?.stylesheets?.add("style/clientStyle.css")
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.isResizable = false
        currentStage?.initStyle(StageStyle.UTILITY)
        currentStage?.isAlwaysOnTop = true
        currentStage?.initModality(Modality.WINDOW_MODAL)
    }
}