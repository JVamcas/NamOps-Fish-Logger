package com.pet001kambala.controller

import com.pet001kambala.model.BinTransaction
import com.pet001kambala.model.BinTransactionModel
import com.pet001kambala.repo.BinTransactionRepo
import com.pet001kambala.utils.Results
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import tornadofx.*

class CurrentTransactionTableController : AbstractModelTableController<BinTransaction>("") {

    private val transModel: BinTransactionModel by inject()

    override val root = vbox {
        scrollpane {
            tableview<BinTransaction> {
                prefHeight = 200.0
                prefWidth = 400.0
                minHeight = prefHeight
                minWidth = prefWidth

                items = modelList
                smartResize()

                column("Bin Number", BinTransaction::binNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                }
                column("Pit Number", BinTransaction::pitNoProperty).apply {
                    style = "-fx-alignment: CENTER;"
                }
                column("Bin Weight (KG)", BinTransaction::binWeightProperty).apply {
                    style = "-fx-alignment: CENTER;"
                }

                placeholder = Label("There are no bins on that waybill yet.")
                columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
                vgrow = Priority.ALWAYS
            }
        }
    }

    override suspend fun loadModels(): ObservableList<BinTransaction> {

        val results = BinTransactionRepo().loadCurrentTransactionBins(transModel.wayBillNo.get())

        return if (results is Results.Success<*>)
            results.data as ObservableList<BinTransaction>
        else {
            parseResults(results)
            observableListOf()
        }
    }

    override fun onDock() {
        super.onDock()
        title = "Bins logged"
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.isResizable = false
        currentStage?.initStyle(StageStyle.UTILITY)
    }
}