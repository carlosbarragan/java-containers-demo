package info.novatec.adapters.db

import info.novatec.utils.DefaultNoArgConstructor
import jakarta.persistence.*

@Entity
@DefaultNoArgConstructor
@NamedQueries(
    NamedQuery(name = "getAuthors", query = "SELECT a FROM Author a"),
)
data class Author(
    var name: String,
    var lastName: String,
    @OneToOne
    var book: Book,
    @Id
    @GeneratedValue
    var id: Long? = null
)