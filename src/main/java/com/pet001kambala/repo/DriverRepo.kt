package com.pet001kambala.repo

import com.pet001kambala.model.Driver
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*

class DriverRepo : AbstractRepo<Driver>() {

    suspend fun findDriver(code: String): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val strqry = "SELECT * FROM drivers where driver_code=:code AND deleted=false"
                val data = session!!.createNativeQuery(strqry, Driver::class.java)
                    .setParameter("code", code)
                    .resultList.filterNotNull()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}