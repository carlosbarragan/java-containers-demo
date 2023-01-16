package info.novatec.adapters.db

import info.novatec.utils.DefaultNoArgConstructor
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
@DefaultNoArgConstructor
data class Book(
    var name:String,
    var isbn:String,
    @Id
    @GeneratedValue
    var id:Long? =null
)
