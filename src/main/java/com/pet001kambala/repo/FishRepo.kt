package com.pet001kambala.repo

import com.pet001kambala.model.Fish
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class FishRepo : AbstractRepo<Fish>() {
    suspend fun loadAll(): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val qryStr = "SELECT * FROM fish"
                val data = session!!.createNativeQuery(qryStr, Fish::class.java).resultList.asObservable()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            session?.close()
            Results.Error(e)
        }
    }

    suspend fun deleteFish(fish: Fish): Results {
        fish.deletedProperty.set(true)

        return updateModel(fish)
    }
}