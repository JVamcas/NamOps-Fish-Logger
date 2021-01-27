package com.pet001kambala.keypad

import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.Region
import java.util.ArrayList
import java.util.function.Consumer

class KeyPad(horizontalGap: Double = 5.0, verticalGap: Double = 5.0) : Region() {

    private val CSS_FILE = "style/numberpad/numberpad.css"
    private val PREFERRED_WIDTH = 340.0
    private val PREFERRED_HEIGHT = 340.0
    private val MINIMUM_WIDTH = PREFERRED_WIDTH
    private val MINIMUM_HEIGHT = PREFERRED_HEIGHT
    private val MAXIMUM_WIDTH = 1024.0
    private val MAXIMUM_HEIGHT = 1024.0
    private var userAgentStyleSheet: String? = null

    //    private var width = 0.0
//    private var height = 0.0
    private var horizontalGap = 0.0
    private var verticalGap = 0.0
    private lateinit var key0: Key
    private lateinit var key1: Key
    private lateinit var key2: Key
    private lateinit var key3: Key
    private lateinit var key4: Key
    private lateinit var key5: Key
    private lateinit var key6: Key
    private lateinit var key7: Key
    private lateinit var key8: Key
    private lateinit var key9: Key
    private lateinit var keyDot: Key
    private lateinit var keyPlus: Key
    private lateinit var keyMinus: Key
    private lateinit var keyDel: Key
    private lateinit var keyBS: Key
    private lateinit var keyUp: Key
    private lateinit var keyRight: Key
    private lateinit var keyDown: Key
    private lateinit var keyLeft: Key
    private lateinit var keyEnter: Key
    private lateinit var keyCancel: Key
    private lateinit var keyClr: Key
    private lateinit var keys: MutableList<Key>
    private lateinit var pane: GridPane

    init {
        width = 0.0
        height = 0.0
        this.horizontalGap = horizontalGap
        this.verticalGap = verticalGap
        initGraphics()
        registerListeners()
    }


    // ******************** Initialization ************************************
    private fun initGraphics() {
        if (java.lang.Double.compare(prefWidth, 0.0) <= 0 || java.lang.Double.compare(
                prefHeight,
                0.0
            ) <= 0 || java.lang.Double.compare(getWidth(), 0.0) <= 0 || java.lang.Double.compare(getHeight(), 0.0) <= 0
        ) {
            if (prefWidth > 0 && prefHeight > 0) {
                setPrefSize(prefWidth, prefHeight)
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT)
            }
        }
        styleClass.add("number-pad")
        keys = ArrayList<Key>()
        key0 = createKey("0", KeyCode.NUMPAD0)
        key1 = createKey("1", KeyCode.NUMPAD1)
        key2 = createKey("2", KeyCode.NUMPAD2)
        key3 = createKey("3", KeyCode.NUMPAD3)
        key4 = createKey("4", KeyCode.NUMPAD4)
        key5 = createKey("5", KeyCode.NUMPAD5)
        key6 = createKey("6", KeyCode.NUMPAD6)
        key7 = createKey("7", KeyCode.NUMPAD7)
        key8 = createKey("8", KeyCode.NUMPAD8)
        key9 = createKey("9", KeyCode.NUMPAD9)
        keyDot = createKey(".", KeyCode.SEPARATOR)
        keyMinus = createKey("-", KeyCode.MINUS)
        keyPlus = createKey("+", KeyCode.PLUS)
        keyDel = createKey("DEL", KeyCode.DELETE)
        keyBS = createKey("BS", KeyCode.BACK_SPACE)
        keyUp = createKey("\u25b2", KeyCode.UP)
        keyRight = createKey("\u25b6", KeyCode.RIGHT)
        keyDown = createKey("\u25bc", KeyCode.DOWN)
        keyLeft = createKey("\u25c0", KeyCode.LEFT)
        keyEnter = createKey("\u23ce", KeyCode.ENTER)
        keyCancel = createKey("CANCEL", KeyCode.CANCEL)
        keyClr = createKey("CLR", KeyCode.CLEAR)
        pane = GridPane()
        pane.hgap = horizontalGap
        pane.vgap = verticalGap
        pane.add(keyLeft, 0, 0)
        pane.add(keyRight, 1, 0)
        pane.add(keyClr, 2, 0)
        pane.add(keyCancel, 3, 0)
        pane.add(key7, 0, 1)
        pane.add(key8, 1, 1)
        pane.add(key9, 2, 1)
        pane.add(keyBS, 3, 1)
        pane.add(keyUp, 4, 1)
        pane.add(key4, 0, 2)
        pane.add(key5, 1, 2)
        pane.add(key6, 2, 2)
        pane.add(keyDel, 3, 2)
        pane.add(keyDown, 4, 2)
        pane.add(key1, 0, 3)
        pane.add(key2, 1, 3)
        pane.add(key3, 2, 3)
        pane.add(keyPlus, 3, 3)
        pane.add(keyEnter, 4, 3)
        pane.add(key0, 0, 4)
        pane.add(keyDot, 2, 4)
        pane.add(keyMinus, 3, 4)
        GridPane.setColumnSpan(key0, 2)
        GridPane.setColumnSpan(keyCancel, 2)
        GridPane.setRowSpan(keyEnter, 2)
        children.setAll(pane)
    }

    private fun registerListeners() {
        widthProperty().addListener { _ -> resize() }
        heightProperty().addListener { _ -> resize() }
    }


    // ******************** Methods *******************************************
    override fun computeMinWidth(HEIGHT: Double): Double {
        return MINIMUM_WIDTH
    }

    override fun computeMinHeight(WIDTH: Double): Double {
        return MINIMUM_HEIGHT
    }

    override fun computeMaxWidth(HEIGHT: Double): Double {
        return MAXIMUM_WIDTH
    }

    override fun computeMaxHeight(WIDTH: Double): Double {
        return MAXIMUM_HEIGHT
    }


    fun setOnKeyPressed(observer: KeyEventObserver) {
        keys.forEach(Consumer { key: Key -> key.setOnKeyPressed(observer) })
    }

    fun removeOnKeyPressed(observer: KeyEventObserver) {
        keys.forEach(Consumer { key: Key -> key.removeOnKeyPressed(observer) })
    }

    fun setOnKeyReleased(observer: KeyEventObserver) {
        keys.forEach(Consumer { key: Key -> key.setOnKeyReleased(observer) })
    }

    fun removeOnKeyReleased(observer: KeyEventObserver) {
        keys.forEach(Consumer { key: Key -> key.removeOnKeyReleased(observer) })
    }

    fun getHorizontalGap(): Double {
        return horizontalGap
    }

    fun setHorizontalGap(horizontalGap: Double) {
        this.horizontalGap = horizontalGap
        pane.hgap = horizontalGap
    }

    fun getVerticalGap(): Double {
        return verticalGap
    }

    fun setVerticalGap(verticalGap: Double) {
        this.verticalGap = verticalGap
        pane.vgap = verticalGap
    }

    private fun createKey(text: String): Key {
        return createKey(text, "number-pad", null)
    }

    private fun createKey(text: String, metaData: KeyCode?): Key {
        return createKey(text, "number-pad", metaData)
    }

    private fun createKey(text: String, styleClass: String?, metaData: KeyCode?): Key {
        val key = Key(text, metaData)
        if (null != styleClass && styleClass.isNotEmpty()) {
            key.styleClass.add(styleClass)
        }
        GridPane.setHalignment(key, HPos.CENTER)
        GridPane.setValignment(key, VPos.CENTER)
        keys.add(key)
        return key
    }


    // ******************** Resizing ******************************************
    private fun resize() {
        if (width != widthProperty().get())
            width = width - insets.left - insets.right
        if (height != heightProperty().get())
            height = height - insets.top - insets.bottom

        if (width > 0 && height > 0) {
            pane.setMaxSize(width, height)
            pane.setPrefSize(width, height)
            pane.relocate((width - width) * 0.5, (height - height) * 0.5)
        }
    }


    // ******************** Style related *************************************
    override fun getUserAgentStylesheet(): String? {
        if (null == userAgentStyleSheet) {
            userAgentStyleSheet = Key::class.java.classLoader.getResource(CSS_FILE).toExternalForm()
        }
        return userAgentStyleSheet
    }
}