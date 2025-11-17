package com.pet001kambala.keypad

import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.beans.Observable
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ObjectPropertyBase
import javafx.beans.property.StringProperty
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.VPos
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import javafx.scene.input.TouchEvent
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory

class Key(keyText: String? = null, metaData: KeyCode? = null) : Region() {

    private val logger = LoggerFactory.getLogger(Key::class.java)

    private val CSS_FILE = "style/numberpad/key.css"
    private val PREFERRED_WIDTH = 64.0
    private val PREFERRED_HEIGHT = 64.0
    private val MINIMUM_WIDTH = 16.0
    private val MINIMUM_HEIGHT = 16.0
    private val MAXIMUM_WIDTH = 1024.0
    private val MAXIMUM_HEIGHT = 1024.0
    private var userAgentStyleSheet: String? = null
    private var observers: ConcurrentHashMap<KeyEventObserver, KeyEventType>? = null
    private lateinit var text: Text
    private lateinit var pane: StackPane
    private var size = 0.0

    private var keyText: String? = null
    private var _metaData: KeyCode? = null
    private var metaData: ObjectProperty<KeyCode>? = null
    private var mouseHandler: EventHandler<MouseEvent>? = null
    private var touchHandler: EventHandler<TouchEvent>? = null


    init {

        height = 0.0
        width = 0.0

        observers = ConcurrentHashMap()
        this.keyText = keyText ?: ""
        _metaData = metaData
        mouseHandler = EventHandler { e: MouseEvent ->
            val type = e.eventType
            if (MouseEvent.MOUSE_PRESSED == type) {
                fireKeyEvent(KeyEvent(this@Key, KeyEventType.PRESSED))
            } else if (MouseEvent.MOUSE_RELEASED == type) {
                fireKeyEvent(KeyEvent(this@Key, KeyEventType.RELEASED))
            }
        }
        touchHandler = EventHandler { e: TouchEvent ->
            val type: EventType<out TouchEvent> = e.eventType
            if (TouchEvent.TOUCH_PRESSED == type) {
                fireKeyEvent(KeyEvent(this@Key, KeyEventType.PRESSED))
            } else if (TouchEvent.TOUCH_RELEASED == type) {
                fireKeyEvent(KeyEvent(this@Key, KeyEventType.RELEASED))
            }
        }
        initGraphics()
        registerListeners()
        logger.debug("Key created with text={}", keyText)
    }

    /***************************************************************************
     *                                                                         *
     * Graphic initialization                                                  *
     *                                                                         *
     **************************************************************************/
    private fun initGraphics() {
        if (prefWidth.compareTo(0.0) <= 0 || prefHeight.compareTo(0.0) <= 0 || width.compareTo(0.0) <= 0 || getHeight().compareTo(
                0.0
            ) <= 0
        ) {
            if (prefWidth > 0 && prefHeight > 0) {
                setPrefSize(prefWidth, prefHeight)
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT)
            }
        }
        text = Text(keyText)
        text.textOrigin = VPos.CENTER
        text.styleClass.add("text")
        pane = StackPane(text)
        pane.styleClass.add("key")
        children.setAll(pane)
    }

    private fun registerListeners() {
        widthProperty().addListener { _: Observable? -> resize() }
        heightProperty().addListener { _: Observable? -> resize() }
        if (Platform.isSupported(ConditionalFeature.INPUT_MULTITOUCH)) {
            addEventHandler(TouchEvent.TOUCH_PRESSED, touchHandler)
            addEventHandler(TouchEvent.TOUCH_RELEASED, touchHandler)
        } else {
            addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler)
            addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler)
        }
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

    private fun getKeyText(): String {
        return text.text
    }

    fun textProperty(): StringProperty {
        return text.textProperty()
    }

    fun metaDataProperty(): ObjectProperty<KeyCode>? {
        if (null == metaData) {

            metaData = object : ObjectPropertyBase<KeyCode>(_metaData) {
                override fun getBean(): Any {
                    return this@Key
                }

                override fun getName(): String {
                    return "metaData"
                }
            }
            _metaData = null
        }
        return metaData
    }

    private fun adjustTextSize() {
        if (getKeyText().length > 3) {
            val charWidth = width * 0.9 / getKeyText().length
            text.font = Fonts.robotoMonoRegular(charWidth * 1.66639544344996)
        } else {
            text.font = Fonts.robotoMonoRegular(size * 0.5)
        }
    }


    // ******************** EventHandling *************************************
    fun setOnKeyPressed(observer: KeyEventObserver) {
        if (!observers!!.containsKey(observer)) {
            observers!![observer] = KeyEventType.PRESSED
        }
    }

    fun removeOnKeyPressed(observer: KeyEventObserver) {
        removeObserver(observer)
    }

    fun setOnKeyReleased(observer: KeyEventObserver) {
        if (!observers!!.containsKey(observer)) {
            observers!![observer] = KeyEventType.RELEASED
        }
    }

    fun removeOnKeyReleased(observer: KeyEventObserver) {
        removeObserver(observer)
    }

    fun removeAllObservers() {
        observers!!.clear()
    }

    private fun removeObserver(observer: KeyEventObserver) {
        if (observers!!.containsKey(observer)) {
            observers!!.remove(observer)
        }
    }

    private fun fireKeyEvent(evt: KeyEvent) {
        observers!!.entries.stream()
            .filter { o: Map.Entry<KeyEventObserver, KeyEventType> -> o.value == evt.type }
            .forEach { entry: Map.Entry<KeyEventObserver, KeyEventType> ->
                entry.key.onKeyEvent(
                    evt
                )
            }
    }

    // ******************** Resizing ******************************************
    private fun resize() {
        width = width - insets.left - insets.right
        height = height - insets.top - insets.bottom
        size = if (width < height) width else height
        if (width > 0 && height > 0) {
            pane.setMinSize(width, height)
            pane.setMaxSize(width, height)
            pane.setPrefSize(width, height)
            pane.relocate((width - width) * 0.5, (height - height) * 0.5)
            adjustTextSize()
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

enum class KeyEventType {
    PRESSED, RELEASED
}

class KeyEvent(val source: Key, val type: KeyEventType)

interface KeyEventObserver {
    fun onKeyEvent(evt: KeyEvent)
}