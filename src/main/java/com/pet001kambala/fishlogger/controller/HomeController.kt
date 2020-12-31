package com.pet001kambala.fishlogger.controller

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import tornadofx.*
import java.io.FileInputStream

class HomeController : AbstractView("") {
    override val root: GridPane by fxml("/view/TransactionView.fxml")

    private val driverIdCodeLabel: Label by fxid()


    init {

//        driverIdCodeLabel.apply {
//            val image = Image(FileInputStream("C:\\Users\\user\\IdeaProjects\\NamOps Bins Logger\\src\\main\\resources\\images\\driverIcon.jpg"))
//            val imageView = ImageView(image)
//            graphic = imageView
//        }
    }


    override fun onDock() {
        super.onDock()
        workspace.header.hide()
        currentStage?.isMaximized = true
    }
}
