package com.pet001kambala.repo

import com.pet001kambala.model.Factory
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import org.slf4j.LoggerFactory
import tornadofx.*

class FactoryRepo: AbstractRepo<Factory>(){

    private val logger = LoggerFactory.getLogger(FactoryRepo::class.java)

    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                logger.debug("Loading all factories")
                session = sessionFactory!!.openSession()
                val qryStr = "SELECT * FROM factories"
                val data = session!!.createNativeQuery(qryStr, Factory::class.java).resultList.asObservable()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            logger.error("Error loading factories", e)
            session?.close()
            Results.Error(e)
        }
    }

    suspend fun deleteFactory(factory: Factory): Results {
        factory.deletedProperty.set(true)

        return updateModel(factory)
    }
}