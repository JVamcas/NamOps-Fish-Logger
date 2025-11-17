package com.pet001kambala.repo


import com.pet001kambala.utils.Results
import com.pet001kambala.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import org.slf4j.LoggerFactory

abstract class AbstractRepo<T> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val sessionFactory by lazy { SessionManager.newInstance }

    open suspend fun addNewModel(model: T): Results {
        var session: Session? = null;
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val transaction = session!!.beginTransaction()
                session!!.persist(model)
                transaction.commit()
                logger.info("Persisted new model: {}", model)
                Results.Success<T>(code = Results.Success.CODE.WRITE_SUCCESS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            session?.transaction?.rollback()
            logger.error("Error persisting model", e)
            Results.Error(e)
        }
        finally {
            session?.close()
        }
    }

    open suspend fun updateModel(model: T): Results {
        var session: Session? = null;
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory!!.openSession()
                val transaction = session!!.beginTransaction()
                session!!.update(model)
                transaction.commit()
                logger.info("Updated model: {}", model)
                Results.Success<T>(code = Results.Success.CODE.WRITE_SUCCESS)
            }
        } catch (e: Exception) {
            session?.transaction?.rollback()
            logger.error("Error updating model", e)
            Results.Error(e)
        }
        finally {
            session?.close()
        }
    }
}