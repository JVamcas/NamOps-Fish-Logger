package com.pet001kambala.utils

import com.pet001kambala.repo.DBConfigRepo
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistry
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager

object SessionManager {

    private val logger = LoggerFactory.getLogger(SessionManager::class.java)

    var newInstance: SessionFactory? = null
    private var registry: StandardServiceRegistry? = null

    init {
        try {
            if (newInstance == null) {
                logger.info("Initializing Hibernate SessionFactory")
                // Create a properties object to hold your Hibernate configurations
                val config = DBConfigRepo().getActiveConfig()


                val builder = StandardServiceRegistryBuilder()
                    .configure() // Load configuration from hibernate.cfg.xml

                if (config != null){
                    logger.info("Active DB config found, applying connection properties for host={}", config.hostProperty.get())

                    val properties = mapOf(
                        "hibernate.connection.url" to "jdbc:mysql://${config.hostProperty.get()}:3306/${config.dbNameProperty.get()}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",
                        "hibernate.connection.username" to config.usernameProperty.get(),
                        "hibernate.connection.password" to config.passwordProperty.get(),
                        // Add more properties as needed
                    )
                    builder.applySettings(properties)// Apply additional properties
                } else {
                    logger.info("No active DB config found, using hibernate.cfg.xml defaults")
                }

                registry = builder.build()

                val metaSrc = MetadataSources(registry)

                val meta = metaSrc.metadataBuilder.build()
                newInstance = meta.sessionFactoryBuilder.build()
                logger.info("Hibernate SessionFactory initialized successfully")
            }
        } catch (e: Exception) {
            logger.error("Error while initializing Hibernate SessionFactory", e)
            shutDown()
        }
    }


    private fun shutDown() {
        registry?.let {
            StandardServiceRegistryBuilder.destroy(it)
            logger.info("Destroyed Hibernate registry during shutdown")
        }
    }
}
