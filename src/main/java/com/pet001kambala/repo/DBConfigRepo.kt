package com.pet001kambala.repo

import com.pet001kambala.model.DBConfig
import com.pet001kambala.utils.ParseUtil.Companion.convert
import com.pet001kambala.utils.Results
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.File
import kotlin.apply
import kotlin.collections.ArrayList
import kotlin.collections.find
import kotlin.collections.map
import kotlin.io.readText
import kotlin.io.writeText
import org.slf4j.LoggerFactory

class DBConfigRepo : AbstractRepo<DBConfig>() {

    private val logger = LoggerFactory.getLogger(DBConfigRepo::class.java)

    private val configFilePath = "seanam_fe_config.ctx"

    fun loadConfigs(): ObservableList<DBConfig> {
        val file = File(configFilePath)
        if (!file.exists()) {
            logger.info("Config file not found at {}, creating default.", configFilePath)
            file.createNewFile()
            val defaultConfigs = FXCollections.observableArrayList<DBConfig>()
            defaultConfigs.add(
                DBConfig().apply {
                    hostProperty.set("")
                    usernameProperty.set("")
                    dbNameProperty.set("")
                    passwordProperty.set("")
                    isActiveProperty.set("")
                }
            )
            saveConfigs(defaultConfigs) // Save an empty list if file didn't exist previously
        }

        val jsonString = file.readText()
        if (jsonString.isBlank()) {
            logger.warn("Config file {} is empty", configFilePath)
            return FXCollections.observableArrayList()
        }
        val arrayList = (jsonString.convert<ArrayList<Map<String, String>>>())
        val configs = arrayList.map {
            DBConfig().apply {
                hostProperty.set(it["host"])
                usernameProperty.set(it["username"])
                dbNameProperty.set(it["dbName"])
                passwordProperty.set(it["password"])
                isActiveProperty.set(it["isActive"])
                idProperty.set(it["id"])
            }
        }
        logger.info("Loaded {} DB config(s) from {}", configs.size, configFilePath)
        return FXCollections.observableArrayList(configs)
    }

    fun getActiveConfig(): DBConfig? {

        val active = loadConfigs().find { it.isActiveProperty.get() == "Y" }
        if (active == null) logger.info("No active DB config found") else logger.info("Active DB config selected: host={}", active.hostProperty.get())
        return active
    }


    private fun saveConfigs(configs: ObservableList<DBConfig>) {
        val jsonString = configs.map { it.toString() }.toString()
        File(configFilePath).writeText(jsonString)
        logger.info("Saved {} config(s) to {}", configs.size, configFilePath)
    }

//    fun addConfig(config: DBConfig): Results {
//        val configs = loadConfigs()
//        config.idProperty.set(generateID(16))
//        configs.add(config)
//        saveConfigs(configs)
//        return Results.Success(data = config, code = Results.Success.CODE.LOAD_SUCCESS)
//    }
//
//    fun deleteConfig(config: DBConfig): Results {
//        val configs = loadConfigs()
//
//        val index = configs.indexOfFirst { it.idProperty.get() == config.idProperty.get() }
//
//        if (index != -1) {
//            configs.removeAt(index)
//            saveConfigs(configs)
//        }
//        return Results.Success(data = config, code = Results.Success.CODE.LOAD_SUCCESS)
//    }
//
//    override suspend fun updateModel(model: DBConfig): Results {
//        val configs = loadConfigs()
//        val idx = configs.indexOfFirst { it.idProperty.get() == model.idProperty.get() }
//
//        if (idx >= 0 && idx < configs.size) {
//            configs[idx] = model
//            saveConfigs(configs)
//        }
//
//        return Results.Success(data = configs, code = Results.Success.CODE.LOAD_SUCCESS)
//    }
}