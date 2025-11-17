package com.pet001kambala.utils


import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.pet001kambala.model.BinTransaction
import com.pet001kambala.model.BinTransactionModel
import com.pet001kambala.model.Factory
import com.pet001kambala.model.Fish
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble
import java.util.Random
import java.util.UUID
import java.util.regex.Pattern
import org.slf4j.LoggerFactory

class ParseUtil {

    companion object {

        private val logger = LoggerFactory.getLogger(ParseUtil::class.java)

        fun String?.isValidPassword() = this != null && this.length >= 4

        fun TextField.numberValidation(msg: String) =
            validator(ValidationTrigger.OnChange()) {
                if (it.isNumber())
                    null else error(msg)
            }

        fun Any?.isNumber(): Boolean {
            val value = this.toString()
            return value != "null" &&
                    try {
                        parseDouble(value)
                        true
                    } catch (e: Exception) {
                        false
                    }
        }

        fun String?.strip(): String {
            return this?.trim() ?: ""
        }

        fun BinTransaction?.copy(): BinTransaction {

            return if (this == null) BinTransaction()
            else
                BinTransaction().also {
                    it.idCodeProperty.set(idCodeProperty.get())
                    it.noOfBinsProperty.set(noOfBinsProperty.get())
                    it.waybillNoProperty.set(waybillNoProperty.get())
                    it.driver = driver
                    it.factory = factory
                    it.fish = fish
                }
        }
        inline fun <I, reified O> I.convert(): O {
            val json = this.toJson()
            return Gson().fromJson(json, object : TypeToken<O>() {}.type)
        }

        inline fun <reified O> String.convert(): O {
            return Gson().fromJson(this, object : TypeToken<O>() {}.type)
        }

        fun <K> K.toJson(): String {
            return Gson().toJson(this)
        }

        fun generateID(length: Int): String {
            val uuid = UUID.randomUUID().toString().replace("-", "")
            val random = Random()
            val buffer = StringBuilder()

            repeat(length) {
                buffer.append(uuid[random.nextInt(uuid.length)])
            }

            return buffer.toString()
        }

        fun String?.isValidIdCode(): Boolean {
            return this.isNumber() && this?.length ?: 0 >= 4
        }

        fun String?.isValidWayBill(): Boolean {
            return this.isNumber() && (this?.length in 5 until 7)
        }

        fun String?.isValidBinWeight(): Boolean {
            return this.isNumber() && this?.toFloat() ?: 0f >= 260f
        }
    }
}