package com.pet001kambala.fishlogger.app

import com.pet001kambala.fishlogger.controller.HomeController
import tornadofx.*

class NamOpsFishBinLogger: App(MainWorkspace::class, Styles::class){


    override fun onBeforeShow(view: UIComponent) {
        super.onBeforeShow(view)
        workspace.dock<HomeController>()
    }
}