package com.pet001kambala.controller

import com.pet001kambala.controller.AbstractView.Error.showError
import com.pet001kambala.utils.Results
import javafx.application.Platform
import javafx.beans.property.Property
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import tornadofx.*

abstract class AbstractView(private val viewTitle: String) : View(viewTitle) {


    fun UIComponent.closeView() {
        Platform.runLater {
            close()
        }
    }

    object Error {
        fun showError(header: String, msg: String) {
            Platform.runLater {
                Alert(Alert.AlertType.ERROR).apply {
                    title = "Error"
                    headerText = header
                    contentText = msg
                    showAndWait()
                }
            }
        }
    }


    fun parseResults(results: Results) {
        if (results is Results.Success<*>) {

        } else if (results is Results.Error) {

            when (results.code) {

                Results.Error.CODE.UNKNOWN -> {
                    showError(
                        header = "Unknown Error", msg = "An unknown error has occurred. \nWhat to do:\n" +
                                "1.  Restart the program.\n" +
                                "2. If the error persists, please contact the system administrator at NamOps Logistics Pty Ltd."
                    )
                }

                Results.Error.CODE.NO_CONNECTION -> {
                    showError(
                        header = "No Connection",
                        msg = "Computer appears to be offline. Please refresh your connections."
                    )
                }
            }
        }
    }

    fun <T> ComboBox<T>.bindCombo(property: Property<T>) {
        bind(property)
        bindSelected(property)
    }

    override fun onDock() {
        super.onDock()
        workspace.saveButton.hide()
        currentStage?.isMaximized = false
        title = "SeaNam Bins Logger"
        heading = viewTitle
    }

//    fun isLoggedIn() = currentUser.get().isInvalid()
//
//    fun logout() {
//        currentUser.set(User())
//    }
}