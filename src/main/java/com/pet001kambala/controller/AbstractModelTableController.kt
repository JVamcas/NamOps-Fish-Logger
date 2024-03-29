package com.pet001kambala.controller

import javafx.collections.ObservableList
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tornadofx.*
import kotlin.reflect.KClass

abstract class AbstractModelTableController<T>(title: String) : AbstractView(title) {

    val modelList = SortedFilteredList<T>()
    private val indexColumn = TableColumn<T, String>("#")

    init {
        indexColumn.apply {
            contentWidth(padding = 5.0, useAsMin = true)
            style = "-fx-alignment: CENTER;"
            cellValueFactory = PropertyValueFactory<T, String>("Index")
            setCellFactory {
                object : TableCell<T?, String>() {
                    override fun updateIndex(index: Int) {
                        super.updateIndex(index)
                        if (isEmpty || index < 0) {
                            setText(null)
                        } else {
                            setText((index + 1).toString())
                        }
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        onRefresh()
    }

    override fun onRefresh() {
        super.onRefresh()
        //load user data here from db
        runBlocking {
            launch {
                val models = loadModels()
                modelList.asyncItems { models }
            }
        }
    }

    abstract suspend fun loadModels(): ObservableList<T>
}

