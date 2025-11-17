package com.pet001kambala.app

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px
import org.slf4j.LoggerFactory

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    private val logger = LoggerFactory.getLogger(Styles::class.java)

    init {
        logger.debug("Initializing Styles stylesheet")
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
    }
}