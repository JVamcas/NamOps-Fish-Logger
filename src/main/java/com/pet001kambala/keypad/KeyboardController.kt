package com.pet001kambala.keypad

import com.pet001kambala.controller.AbstractView
import com.pet001kambala.model.TextModel
import com.pet001kambala.utils.ParseUtil.Companion.isNumber
import javafx.scene.input.KeyCode
import javafx.stage.Modality
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory
import tornadofx.*

class KeyboardController : AbstractView("Keyboard") {

    private val logger = LoggerFactory.getLogger(KeyboardController::class.java)

    val tModel: TextModel by inject()

    override val root = stackpane {
        add(KeyPad().apply {

            setOnKeyPressed(object : KeyEventObserver {
                override fun onKeyEvent(evt: KeyEvent) {
                    var curTxt = tModel.property.get() ?: ""
                    val keyTxt = evt.source.textProperty().get()
                    val kMeta = evt.source.metaDataProperty()?.get()

                    curTxt = when {
                        kMeta == KeyCode.SEPARATOR || keyTxt.isNumber() -> curTxt + keyTxt
                        kMeta == KeyCode.DELETE || kMeta == KeyCode.BACK_SPACE -> curTxt.substring(
                            0,
                            if (curTxt.isEmpty()) 0 else curTxt.length - 1
                        )
                        kMeta == KeyCode.CLEAR -> ""
                        kMeta == KeyCode.ENTER || kMeta == KeyCode.CANCEL -> {
                            closeView()
                            curTxt
                        }
                        else -> curTxt
                    }
                    tModel.property.set(curTxt)
                }
            })
        })
    }


    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.isResizable = false
        currentStage?.initStyle(StageStyle.UTILITY)
        currentStage?.isAlwaysOnTop = true
        currentStage?.initModality(Modality.WINDOW_MODAL)
        logger.debug("KeyboardController about to show")
    }
}