package com.pet001kambala.utils


import com.pet001kambala.model.BinTransaction
import com.pet001kambala.model.BinTransactionModel
import com.pet001kambala.model.Factory
import com.pet001kambala.model.Fish
import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble
import java.util.regex.Pattern

class ParseUtil {

    companion object {


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