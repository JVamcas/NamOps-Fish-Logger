package com.pet001kambala.app

import com.pet001kambala.controller.HomeController
import com.pet001kambala.utils.SessionManager
import tornadofx.*

class ClientApp: App(MainWorkspace::class, Styles::class){

    override fun onBeforeShow(view: UIComponent) {
        super.onBeforeShow(view)
        workspace.dock<HomeController>()
    }

    override fun stop() {
        super.stop()
        try{
            SessionManager.newInstance!!.close()
            println("Session factory closed")
        }
        catch (e: Exception){

        }
    }
}