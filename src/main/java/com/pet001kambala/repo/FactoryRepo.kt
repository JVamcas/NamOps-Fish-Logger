package com.pet001kambala.repo

import com.pet001kambala.model.Factory
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class FactoryRepo: AbstractRepo<Factory>(){

    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val qryStr = "SELECT * FROM factories"
                val data = session!!.createNativeQuery(qryStr, Factory::class.java).resultList.asObservable()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            session?.close()
            Results.Error(e)
        }
    }

    suspend fun deleteFactory(factory: Factory): Results {
        factory.deletedProperty.set(true)

        return updateModel(factory)
    }
}