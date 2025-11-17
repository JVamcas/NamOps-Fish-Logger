package com.pet001kambala.keypad

import javafx.scene.text.Font
import java.net.URL
import org.slf4j.LoggerFactory

object Fonts {

    private val logger = LoggerFactory.getLogger(Fonts::class.java)

    lateinit var robotoMonoRegularName: String
    lateinit var ROBOTO_MONO_REGULAR_NAME: String
    init {
        try {
            val fontPath = Fonts::class.java.classLoader.getResource("style/numberpad/RobotoMono-Regular.ttf").toExternalForm()
            robotoMonoRegularName = Font.loadFont(URL(fontPath).openStream(), 10.0).name
            ROBOTO_MONO_REGULAR_NAME = robotoMonoRegularName
            logger.debug("Loaded Roboto Mono font: {}", robotoMonoRegularName)
        }
        catch (e: Exception){
            logger.error("Error loading font RobotoMono-Regular.ttf", e)
        }
    }

    fun robotoMonoRegular(SIZE: Double): Font {
        return Font(ROBOTO_MONO_REGULAR_NAME, SIZE)
    }
}