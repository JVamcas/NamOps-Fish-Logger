package com.pet001kambala.model

import com.pet001kambala.utils.SimpleBooleanConvertor
import com.pet001kambala.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.persistence.*


@Entity
@Table(name = "drivers")
class Driver(
    firstName: String? = null,
    lastName: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "first_name", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val firstNameProperty = SimpleStringProperty(firstName)

    @Column(name = "last_name", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val lastNameProperty = SimpleStringProperty(lastName)

    @Column(name = "driver_code", nullable = false)
    @Convert(converter = SimpleStringConvertor::class)
    val driverCodeProperty = SimpleStringProperty()

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(false)

    override fun toString(): String {
        return "${firstNameProperty.get()} ${lastNameProperty.get()}"
    }
}

class DriverModel : ItemViewModel<Driver>() {
    val firstName = bind(Driver::firstNameProperty)
    val lastName = bind(Driver::lastNameProperty)
    val driverCode = bind(Driver::driverCodeProperty)
    val deleted = bind(Driver::deletedProperty)
}