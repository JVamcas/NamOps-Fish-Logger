package com.pet001kambala.keypad

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlin.system.exitProcess

object Demo : Application() {

    private lateinit var keyPad: KeyPad

    override fun init() {
        keyPad = KeyPad(5.0, 5.0)
        val pressedObserver = object : KeyEventObserver {
            override fun onKeyEvent(evt: KeyEvent) {
                println(
                    evt.source.getMetaData().toString() + " pressed"
                )
            }
        }
        val releasedObserver = object : KeyEventObserver {
            override fun onKeyEvent(evt: KeyEvent) {
                println(
                    evt.source.getMetaData().toString() + " released"
                )
            }
        }
        keyPad.setOnKeyPressed(pressedObserver)
        keyPad.setOnKeyReleased(releasedObserver)
    }

    override fun start(stage: Stage) {
        val pane = StackPane(keyPad)
        val scene = Scene(pane)
        stage.title = "NumberPad"
        stage.scene = scene
        stage.show()
    }

    override fun stop() {
        exitProcess(0)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        launch(*args)
    }
}