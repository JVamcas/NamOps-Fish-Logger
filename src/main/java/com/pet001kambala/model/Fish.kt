package com.pet001kambala.model

import com.pet001kambala.utils.SimpleBooleanConvertor
import com.pet001kambala.utils.SimpleStringConvertor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.persistence.*

@Entity
@Table(name = "fish")
class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column(name = "fish_name")
    @Convert(converter = SimpleStringConvertor::class)
    val nameProperty = SimpleStringProperty()

    @Column(name = "deleted", nullable = false)
    @Convert(converter = SimpleBooleanConvertor::class)
    val deletedProperty = SimpleBooleanProperty(false)

    override fun toString(): String {

        return nameProperty.get()
    }
}

class FishModel: ItemViewModel<Fish>(){
    val name = bind(Fish::nameProperty)
}