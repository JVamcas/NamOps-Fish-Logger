package com.pet001kambala.keypad

import javafx.scene.text.Font
import java.net.URL

object Fonts {

    lateinit var robotoMonoRegularName: String
    lateinit var ROBOTO_MONO_REGULAR_NAME: String
    init {
        try {
            val fontPath = Fonts::class.java.classLoader.getResource("style/numberpad/RobotoMono-Regular.ttf").toExternalForm()
            robotoMonoRegularName = Font.loadFont(URL(fontPath).openStream(), 10.0).name
            ROBOTO_MONO_REGULAR_NAME = robotoMonoRegularName
        }
        catch (e: Exception){
            println(e.printStackTrace())
        }
    }


    // ******************** Methods *******************************************
    fun robotoMonoRegular(SIZE: Double): Font {
        return Font(ROBOTO_MONO_REGULAR_NAME, SIZE)
    }
}