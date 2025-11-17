package com.pet001kambala.repo

import com.pet001kambala.model.Driver
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import org.slf4j.LoggerFactory
import tornadofx.*

class DriverRepo : AbstractRepo<Driver>() {

    private val logger = LoggerFactory.getLogger(DriverRepo::class.java)

    suspend fun findDriver(code: String): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                logger.debug("Finding driver with code={}", code)
                session = sessionFactory!!.openSession()
                val strqry = "SELECT * FROM drivers where driver_code=:code AND deleted=false"
                val data = session!!.createNativeQuery(strqry, Driver::class.java)
                    .setParameter("code", code)
                    .resultList.filterNotNull()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            logger.error("Error finding driver code={}", code, e)
            Results.Error(e)
        } finally {
            session?.close()
        }
    }
}