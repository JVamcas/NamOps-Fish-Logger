package com.pet001kambala.utils

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortEvent
import com.fazecast.jSerialComm.SerialPortPacketListener
import com.pet001kambala.utils.ParseUtil.Companion.isNumber
import javafx.beans.property.SimpleStringProperty
import java.nio.charset.Charset
import java.util.regex.Pattern


class WeighingScaleReader(private val property: SimpleStringProperty) {

    private val comPort: SerialPort = SerialPort.getCommPort("COM3")
    private val pattern: Pattern = Pattern.compile(".*\\+(.+)?(\\d+).*kg")

    init {

        //[2400,7,n,1]

        comPort.baudRate = 2400
        /**Need to get this right as your scale might be very slow*/
        comPort.parity = SerialPort.NO_PARITY
        comPort.numStopBits = SerialPort.ONE_STOP_BIT
        comPort.numDataBits = 7 /*7*/
    }

    fun read() {
        comPort.openPort()
        comPort.addDataListener(object : SerialPortPacketListener {
            override fun getListeningEvents() = SerialPort.LISTENING_EVENT_DATA_RECEIVED

            override fun serialEvent(event: SerialPortEvent) {
                try{
                    val newData = String(event.receivedData, Charset.forName("UTF-8"))

                    println("Data: $newData")

                    val matcher = pattern.matcher(newData)
                    if (matcher.matches()) {
                        val weight = if (!matcher.group(2).isNumber()) 0 else matcher.group(2).toInt()
                        property.set(weight.toString())
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun getPacketSize() = 17

        })
    }
}


