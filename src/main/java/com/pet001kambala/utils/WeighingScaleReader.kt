package com.pet001kambala.utils

import com.fazecast.jSerialComm.*
import com.pet001kambala.utils.ParseUtil.Companion.isNumber
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.sync.Semaphore
import java.nio.charset.Charset
import java.util.regex.Pattern


class WeighingScaleReader(private val property: SimpleStringProperty) {

    private val comPort: SerialPort = SerialPort.getCommPort("COM3")
    private val pattern: Pattern = Pattern.compile("\\+(\\d+)")

    init {

        //[2400,7,n,1]

        comPort.baudRate = 2400
        /**Need to get this right as your scale might be very slow*/
        comPort.parity = SerialPort.NO_PARITY
        comPort.numStopBits = SerialPort.ONE_STOP_BIT
        comPort.numDataBits = 7
    }

    fun read() {
        comPort.addDataListener(object : SerialPortMessageListener {
            override fun getListeningEvents() = SerialPort.LISTENING_EVENT_DATA_RECEIVED

            override fun serialEvent(p0: SerialPortEvent) {
                if (p0.eventType == SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                    val data = p0.receivedData
                    if (data.size == 17) {
                        val weightArray = data.copyOfRange(2, 8)
                        val weight = String(weightArray, Charset.forName("UTF-8")).toInt()
                        Platform.runLater { property.set("$weight KG") }
                    }
                }
            }

            override fun getMessageDelimiter() = byteArrayOf(0x0d)

            override fun delimiterIndicatesEndOfMessage() = true


        })
        comPort.openPort()

    }
}


