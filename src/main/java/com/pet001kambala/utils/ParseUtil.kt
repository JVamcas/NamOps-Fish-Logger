package com.pet001kambala.utils


import javafx.scene.control.TextField
import tornadofx.*
import java.lang.Double.parseDouble
import java.util.regex.Pattern

class ParseUtil {

    companion object {

        fun String?.isValidPlateNo(): Boolean {
            val pattern = Pattern.compile("^N\\d+[A-Z]+$")
            return !this.isNullOrEmpty() && pattern.matcher(this).matches()
        }

        fun String?.isValidVehicleNo(): Boolean {
            val pattern = Pattern.compile("^[HGL]\\d{2,}$")
            return !this.isNullOrEmpty() && pattern.matcher(this).matches()
        }

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

        fun String?.isValidIdCode(): Boolean {
            return this.isNumber() && this?.length ?: 0 >= 4
        }

        fun String?.isValidWayBill(): Boolean {
            return this.isNumber() && this?.length == 5
        }

        fun String?.isValidBinWeight(): Boolean {
            return this.isNumber() && this?.toFloat() ?: 0f >= 300f
        }
    }
}