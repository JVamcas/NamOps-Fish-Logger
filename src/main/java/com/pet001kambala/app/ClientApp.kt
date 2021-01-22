package com.pet001kambala.app

import com.pet001kambala.controller.HomeController
import tornadofx.*

class ClientApp: App(MainWorkspace::class, Styles::class){

    override fun onBeforeShow(view: UIComponent) {
        super.onBeforeShow(view)
        workspace.dock<HomeController>()
    }
}