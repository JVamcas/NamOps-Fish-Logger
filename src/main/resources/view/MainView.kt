package com.pet001kambala.fishlogger.view

import com.pet001kambala.fishlogger.app.Styles
import tornadofx.*

class MainView : View("Hello TornadoFX") {
    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}