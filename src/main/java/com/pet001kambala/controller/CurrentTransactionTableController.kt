package com.pet001kambala.controller

import com.pet001kambala.model.BinTransaction
import com.pet001kambala.model.BinTransactionModel
import com.pet001kambala.repo.BinTransactionRepo
import com.pet001kambala.utils.Results
import com.pet001kambala.utils.SessionManager
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import tornadofx.*

class CurrentTransactionTableController : AbstractModelTableController<BinTransaction>("") {

    override val root = vbox {
        scrollpane {
            tableview<BinTransaction> {
                prefHeight = 200.0
                prefWidth = 600.0
                minHeight = prefHeight
                minWidth = prefWidth

                items = modelList
                smartResize()

                column("Date", BinTransaction::dateProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 5.0, useAsMin = true)
                }
                column("Waybill", BinTransaction::waybillNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 5.0, useAsMin = true)
                }
                column("Bin", BinTransaction::binNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 5.0, useAsMin = true)
                }
                column("Pit", BinTransaction::pitNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 5.0, useAsMin = true)
                }
                column("Bin weight (KG)", BinTransaction::binWeightProperty).apply {
                    style = "-fx-alignment: CENTER;"
                    contentWidth(padding = 5.0, useAsMin = true)
                }
                column("Factory", BinTransaction::factory).apply {
                    contentWidth(padding = 5.0, useAsMin = true)
                    remainingWidth()
                }

                placeholder = Label("There are no bins on that waybill yet.")
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                vgrow = Priority.ALWAYS
            }
        }
    }

    override suspend fun loadModels(): ObservableList<BinTransaction> {

        val results = BinTransactionRepo().loadCurrentTransactionBins()

        return if (results is Results.Success<*>)
            results.data as ObservableList<BinTransaction>
        else {
            parseResults(results)
            observableListOf()
        }
    }

    override fun onDock() {
        super.onDock()
        title = "Recent bins"
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.isResizable = false
        currentStage?.initStyle(StageStyle.UTILITY)
    }
}