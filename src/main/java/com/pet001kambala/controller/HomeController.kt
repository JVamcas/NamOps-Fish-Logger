package com.pet001kambala.controller

import com.pet001kambala.keypad.KeyboardController
import com.pet001kambala.model.*
import com.pet001kambala.repo.BinTransactionRepo
import com.pet001kambala.repo.DriverRepo
import com.pet001kambala.repo.FactoryRepo
import com.pet001kambala.repo.FishRepo
import com.pet001kambala.utils.DateUtil.Companion.localDateToday
import com.pet001kambala.utils.ParseUtil.Companion.isValidBinWeight
import com.pet001kambala.utils.ParseUtil.Companion.isValidIdCode
import com.pet001kambala.utils.ParseUtil.Companion.isValidWayBill
import com.pet001kambala.utils.ParseUtil.Companion.strip
import com.pet001kambala.utils.Results
import com.pet001kambala.utils.WeighingScaleReader
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.GridPane
import kotlinx.coroutines.GlobalScope
import tornadofx.*
import java.sql.Timestamp
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class HomeController : AbstractView("") {

    private val idCode: TextField by fxid("iDcode")
    private val pit_1: Button by fxid("pit_1")
    private val pit_2: Button by fxid("pit_2")
    private val pit_3: Button by fxid("pit_3")
    private val pit_4: Button by fxid("pit_4")
    private val factory: ComboBox<Factory> by fxid("factoryName")
    private val waybillNo: TextField by fxid("waybillNo")
    private val noOfBins: TextField by fxid("noOfBins")
    private val fishType: ComboBox<Fish> by fxid("fishType")
    private val binWeight: TextField by fxid("binWeight")
    private val pendingBins: Label by fxid("pendingBins")
    private val historyBtn: Button by fxid("historyBtn")

    override val root: GridPane by fxml("/view/client/TransactionView.fxml")
    private val transactionRepo = BinTransactionRepo()
    private val transModel = BinTransactionModel()
    private var binLogged = 0

    init {
        transModel.item

        val validDriver = AtomicBoolean(false);
        idCode.apply {
            bind(transModel.idCode)
            validator(ValidationTrigger.OnChange()) {
                if (validDriver.get())
                    null
                else error("No driver exist with that code.")
            }

            setOnMouseClicked {
                showKeyPad(textProperty())
            }

            textProperty().addListener { _, oldCode, newIdCode ->
                if (oldCode.strip() != newIdCode.strip() && newIdCode.strip().isValidIdCode()) {
                    GlobalScope.launch {
                        val results = DriverRepo().findDriver(newIdCode)
                        Platform.runLater {
                            var driver: Driver? = null
                            if (results is Results.Success<*>) {
                                val driverList = results.data as List<Driver>
                                if (driverList.isNotEmpty()) {
                                    validDriver.set(true)
                                    driver = driverList.firstOrNull()
                                    transModel.driver.value = driver
                                } else validDriver.set(false)
                            } else {
                                validDriver.set(false)
                                parseResults(results)
                            }

                            idCode.text = driver?.toString() ?: "$newIdCode "// trigger validation of driver Id code
                            idCode.positionCaret(idCode.text.length)
                        }
                    }
                }
            }
        }


        factory.apply {
            bind(transModel.factory)
            GlobalScope.launch {
                val results = FactoryRepo().loadAll()
                Platform.runLater {
                    if (results is Results.Success<*>) {
                        val factoryList = results.data as ObservableList<Factory>
                        items = factoryList
                        factory.value = factoryList.firstOrNull()
                    }
                }
            }
            required(ValidationTrigger.OnChange(), "Select source factory for these fish.")
        }

        waybillNo.apply {
            bind(transModel.wayBillNo)
            validator(ValidationTrigger.OnChange()) {
                val waybill = waybillNo.text
                if (waybill.isValidWayBill())
                    null
                else error("Waybill number should be 5 characters long.")
            }
            setOnMouseClicked {
                showKeyPad(textProperty())
            }
        }

        noOfBins.apply {
            bind(transModel.noOfBins)
            validator(ValidationTrigger.OnChange()) {
                val bins = if (!it.isNullOrEmpty()) it.toInt() else 0
                when {
                    bins < binLogged -> error("Value cannot be less than bins already recorded.")
                    bins == 0 -> error("Enter a valid number of bins.")
                    else -> {
                        pendingBins.text = (bins - binLogged).toString()
                        null
                    }
                }
            }
            setOnMouseClicked {
                showKeyPad(textProperty())
            }
        }

        fishType.apply {
            bind(transModel.fish)
            GlobalScope.launch {
                val results = FishRepo().loadAll()
                Platform.runLater {
                    if (results is Results.Success<*>) {
                        val fishList = results.data as ObservableList<Fish>
                        items = fishList
                        fishType.value = fishList.firstOrNull()
                    }
                }
            }
        }

        binWeight.apply {
            bind(transModel.binWeight)
            validator(ValidationTrigger.OnChange()) {
                val text = binWeight.text
                if (text.isValidBinWeight())
                    null
                else error("Bin weight must be greater than or equal to 260KG.")
            }
            setOnMouseClicked {
                showKeyPad(textProperty())
            }
            //automatically read weight from the scale
            WeighingScaleReader(transModel.binWeight).read()
        }

        pit_1.apply {
            enableWhen { transModel.valid }
            onMouseClicked = EventHandler { event ->
                if (event?.button == MouseButton.PRIMARY) {
                    if (event.clickCount == 2) {
                        saveTransaction("1")
                    }
                }
            }
        }
        pit_2.apply {
            enableWhen { transModel.valid }
            onMouseClicked = EventHandler { event ->
                if (event?.button == MouseButton.PRIMARY) {
                    if (event.clickCount == 2) {
                        saveTransaction("2")
                    }
                }
            }
        }
        pit_3.apply {
            enableWhen { transModel.valid }
            onMouseClicked = EventHandler { event ->
                if (event?.button == MouseButton.PRIMARY) {
                    if (event.clickCount == 2) {
                        saveTransaction("3")
                    }
                }
            }
        }
        pit_4.apply {
            enableWhen { transModel.valid }
            onMouseClicked = EventHandler { event ->
                if (event?.button == MouseButton.PRIMARY) {
                    if (event.clickCount == 2) {
                        saveTransaction("4")
                    }
                }
            }
        }

        historyBtn.apply {
            action {
//                val waybill = waybillNo.text
//                if (!waybill.isNullOrEmpty()) {
                    setInScope(transModel, scope)
                    find(CurrentTransactionTableController::class, scope).openModal()
//                }
            }
        }
    }

    private fun showKeyPad(property: StringProperty) {
        val scope = Scope()
        val model = TextModel(property)
        setInScope(model, scope)
        find(KeyboardController::class, scope).openModal()
    }

    private fun saveTransaction(pitNo: String) {
        transModel.commit()
        val trans = transModel.item.also {
            it.pitNoProperty.set(pitNo)
            it.dateProperty.set(Timestamp.valueOf(localDateToday()))
            it.binNoProperty.set((binLogged + 1).toString())
        }

        GlobalScope.launch {
            val results = transactionRepo.addNewModel(trans)
            if (results is Results.Success<*>) {
                binLogged++
                Platform.runLater {
                    val pendingBins = noOfBins.text.toInt() - binLogged
                    this@HomeController.pendingBins.text = pendingBins.toString()
                    if (pendingBins == 0) {
                        binLogged = 0
                        transModel.reset(factory.items.first(), fishType.items.first())
                        idCode.clear()
                        noOfBins.clear()
                    }
                }
                transModel.toNextBin()
            } else
                parseResults(results)
        }
    }

    override fun onDock() {
        super.onDock()
        workspace.header.hide()
        currentStage?.isMaximized = true
    }
}
