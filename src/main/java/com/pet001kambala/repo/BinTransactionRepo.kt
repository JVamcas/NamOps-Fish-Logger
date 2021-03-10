package com.pet001kambala.repo

import com.pet001kambala.model.BinTransaction
import com.pet001kambala.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hibernate.Session
import tornadofx.*


class BinTransactionRepo : AbstractRepo<BinTransaction>() {

    suspend fun loadCurrentTransactionBins(waybill: String): Results {

        var session: Session? = null
        return try {
            withContext(Dispatchers.Default) {
                session = sessionFactory?.openSession()
//                val qryStr = "SELECT  * FROM bins_transactions t WHERE t.waybill_number=(SELECT waybill_number FROM bins_transactions ORDER BY id DESC LIMIT 1)"
                val qryStr = "SELECT  * FROM bins_transactions t WHERE t.waybill_number=:waybill"
                val data =
                    session
                        ?.createNativeQuery(qryStr, BinTransaction::class.java)
                        ?.setParameter("waybill", waybill)
                        ?.resultList
                        ?.filterNotNull()
                        ?.toObservable()
                Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
            }
        } catch (e: Exception) {
            Results.Error(e)
        } finally {
            session?.close()
        }
    }

    suspend fun findBinTransaction(waybill: String, binNumber: String): Results {
        var session: Session? = null
        return try {
            withContext(Dispatchers.Default){
                val qryStr = "SELECT * FROM bins_transactions t WHERE t.waybill_number=:waybill and t.bin_number=:binNumber"
                session = sessionFactory?.openSession()
                val data = session
                    ?.createNativeQuery(qryStr,BinTransaction::class.java)
                    ?.setParameter("waybill",waybill)
                    ?.setParameter("binNumber",binNumber)
                    ?.resultList
                    ?.filterNotNull()
                    ?.toObservable()
                Results.Success(data = data,code= Results.Success.CODE.LOAD_SUCCESS)
            }
        }
        catch (e: Exception){
            Results.Error(e)
        }
        finally {
            session?.close()
        }
    }
}