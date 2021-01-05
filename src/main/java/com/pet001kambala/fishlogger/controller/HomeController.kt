package com.pet001kambala.fishlogger.controller

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import tornadofx.*
import java.io.FileInputStream

class HomeController : AbstractView("") {
    override val root: GridPane by fxml("/view/TransactionView.fxml")

    private val driverIdCodeLabel: Label by fxid()
    private val pit_1: Button by fxid("pit_1")
    private val pit_2: Button by fxid("pit_2")
    private val pit_3: Button by fxid("pit_2")
    private val pit_4: Button by fxid("pit_2")


    init {

//        pit_1.apply {
//            val image = Image(FileInputStream("C:\\Users\\user\\IdeaProjects\\NamOps Bins Logger\\src\\main\\resources\\styles\\images\\pit_icon.png"))
//            val imageView = ImageView(image)
//            val ratio = image.width / image.height
//
//            imageView.fitWidth = width
//            imageView.fitHeight = width / ratio
//            imageView.isPreserveRatio = true
//
//            graphic = imageView
//
//        }

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
