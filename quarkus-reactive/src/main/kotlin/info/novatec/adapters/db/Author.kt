package info.novatec.adapters.db

import info.novatec.utils.DefaultNoArgConstructor
import javax.persistence.*

@Entity
@DefaultNoArgConstructor
data class Author(
    var name: String,
    var lastName: String,
    @OneToOne
    var book: Book,
    @Id
    @GeneratedValue
    var id: Long? = null
)
