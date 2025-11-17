package com.pet001kambala.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import kotlin.collections.joinToString
import org.slf4j.LoggerFactory

class DBConfig {

    private val logger = LoggerFactory.getLogger(DBConfig::class.java)


    val hostProperty = SimpleStringProperty()
    val dbNameProperty = SimpleStringProperty()
    val passwordProperty = SimpleStringProperty()
    val usernameProperty = SimpleStringProperty()
    val idProperty = SimpleStringProperty()
    val isActiveProperty = SimpleStringProperty()


    override fun toString(): String {

        val jsonFields = mutableListOf<String>()
        jsonFields.add("\"host\": \"${hostProperty.get() ?: ""}\"")
        jsonFields.add("\"username\": \"${usernameProperty.get() ?: ""}\"")
        jsonFields.add("\"password\": \"${passwordProperty.get() ?: ""}\"")
        jsonFields.add("\"dbName\": \"${dbNameProperty.get() ?: ""}\"")
        jsonFields.add("\"id\": \"${idProperty.get() ?: ""}\"")
        jsonFields.add("\"isActive\": \"${isActiveProperty.get() ?: ""}\"")

        return "{ ${jsonFields.joinToString(", ")} }"

    }

}


class DBConfigModel : ItemViewModel<DBConfig>() {

    val host = bind(DBConfig::hostProperty)
    val dbName = bind(DBConfig::dbNameProperty)
    val password = bind(DBConfig::passwordProperty)
    val username = bind(DBConfig::usernameProperty)
    val isActive = bind(DBConfig::isActiveProperty)
}
