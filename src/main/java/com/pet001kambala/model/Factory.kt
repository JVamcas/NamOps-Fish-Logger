package com.pet001kambala.model

import com.pet001kambala.utils.SimpleBooleanConvertor
import com.pet001kambala.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.persistence.*


@Entity
@Table(name = "factories")
class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "factory_name")
    @Convert(converter = SimpleStringConvertor::class)
    val nameProperty = SimpleStringProperty()

    @Column(name = "deleted")
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty()

    override fun toString(): String {
        return nameProperty.get()
    }
}

class FactoryModel: ItemViewModel<Factory>(){

    val name =  bind(Factory::nameProperty)
}