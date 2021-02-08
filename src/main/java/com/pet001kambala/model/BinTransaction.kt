package com.pet001kambala.model

import com.pet001kambala.utils.ParseUtil.Companion.copy
import com.pet001kambala.utils.SimpleDateConvertor
import com.pet001kambala.utils.SimpleStringConvertor
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.*
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "bins_transactions")
class BinTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Transient
    val driverProperty = SimpleObjectProperty<Driver>()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    var driver: Driver? = null
        set(value) {
            field = value
            driverProperty.set(value)
        }

    @Transient
    val factoryProperty = SimpleObjectProperty<Factory>()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "factory_id", nullable = false)
    var factory: Factory? = null
        set(value) {
            field = value
            factoryProperty.set(value)
        }

    @Column(name = "transaction_date", nullable = false)
    @Convert(converter = SimpleDateConvertor::class)
    val dateProperty = SimpleObjectProperty<Timestamp>()

    @Column(name = "waybill_number")
    @Convert(converter = SimpleStringConvertor::class)
    val waybillNoProperty = SimpleStringProperty()

    @Column(name = "bin_number")
    @Convert(converter = SimpleStringConvertor::class)
    val binNoProperty = SimpleStringProperty()

    @Column(name = "bin_weight")
    @Convert(converter = SimpleStringConvertor::class)
    val binWeightProperty = SimpleStringProperty()

    @Column(name = "pit_number")
    @Convert(converter = SimpleStringConvertor::class)
    val pitNoProperty = SimpleStringProperty()

    @Transient
    val fishProperty = SimpleObjectProperty<Fish>()

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fish_id", nullable = false)
    var fish: Fish? = null
        set(value) {
            field = value
            fishProperty.set(value)
        }

    @Transient
    val idCodeProperty = SimpleStringProperty()

    @Transient
    val noOfBinsProperty = SimpleStringProperty()

    fun data() = arrayListOf(
        Pair("Date", dateProperty.get()),
        Pair("Waybill", waybillNoProperty.get()),
        Pair("Bin", binNoProperty.get()),
        Pair("Bin Weight (KG)", binWeightProperty.get()),
        Pair("Pit", pitNoProperty.get()),
        Pair("Type of fish", fish),
        Pair("Driver", driver),
        Pair("Factory", factory)
    )
}

class BinTransactionModel : ItemViewModel<BinTransaction>() {

    val driver = bind(BinTransaction::driver)
    val factory = bind(BinTransaction::factory)
    val fish = bind(BinTransaction::fish)
    val noOfBins = bind(BinTransaction::noOfBinsProperty)
    val idCode = bind(BinTransaction::idCodeProperty)
    val wayBillNo = bind(BinTransaction::waybillNoProperty)
    val binWeight = bind(BinTransaction::binWeightProperty)

    init {
        item = BinTransaction()
    }

    fun toNextBin() {
        item = item.copy()
    }

    fun reset(defaultFact: Factory, defaultFish: Fish) {
        item = BinTransaction().apply {
            factory = defaultFact
            fish = defaultFish
        }
    }
}

class TextModel(val property: StringProperty): ItemViewModel<String>()