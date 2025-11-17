package com.pet001kambala.app

import com.pet001kambala.controller.HomeController
import com.pet001kambala.utils.SessionManager
import org.slf4j.LoggerFactory
import tornadofx.*

class ClientApp: App(MainWorkspace::class, Styles::class){

    private val logger = LoggerFactory.getLogger(ClientApp::class.java)

    override fun onBeforeShow(view: UIComponent) {
        super.onBeforeShow(view)
        logger.info("Showing main workspace and docking HomeController")
        workspace.dock<HomeController>()
    }

    override fun stop() {
        super.stop()
        logger.info("Application stopping: closing session factory if available")
        try{
            SessionManager.newInstance?.close()
            logger.info("Session factory closed")
        }
        catch (e: Exception){
            logger.error("Error while closing session factory", e)
        }
    }
}