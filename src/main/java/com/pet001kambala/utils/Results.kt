package com.pet001kambala.utils

import com.mysql.cj.protocol.SocketConnection
import java.net.ConnectException
import java.net.SocketException
import org.slf4j.LoggerFactory


sealed class Results {

    companion object {
        fun loading() = Loading
    }

    object Loading : Results()

    class Success<T>(
            val data: T? = null,
            val code: CODE
    ) : Results() {
        enum class CODE {
            WRITE_SUCCESS,
            UPDATE_SUCCESS,
            LOAD_SUCCESS,
            DELETE_SUCCESS,
        }
    }

    class Error(e: Exception) : Results() {
        companion object { private val logger = LoggerFactory.getLogger(Results::class.java) }

        enum class CODE {
            DUPLICATE_WAYBILL,
            NO_CONNECTION,
            UNKNOWN
        }

        val code: CODE = when (e) {
            is DuplicateWaybillException -> CODE.DUPLICATE_WAYBILL
            is SocketException -> CODE.NO_CONNECTION
            is ConnectException -> CODE.NO_CONNECTION
            else -> {
                logger.error("Unhandled exception in Results.Error:", e)
                CODE.UNKNOWN
            }
        }

        class DuplicateVehicleException : Exception()
        class InvalidOdoMeterException : Exception()
        class InsufficientFuelException: Exception()
        class DuplicateWaybillException: Exception()
    }
}