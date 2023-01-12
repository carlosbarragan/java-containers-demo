package info.novatec.adapters.db

import info.novatec.utils.DefaultNoArgConstructor
import javax.persistence.*

@Entity
@DefaultNoArgConstructor
data class Book(
    var name:String,
    var isbn:String,
    @Id
    @GeneratedValue
    var id:Long? =null
)
