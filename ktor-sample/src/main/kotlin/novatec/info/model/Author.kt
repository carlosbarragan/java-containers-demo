package novatec.info.model

import org.jetbrains.exposed.sql.Table

data class Author(
    val name: String,
    val lastName: String,
    val book: Book,
    val id: Int
)

object AuthorsTable: Table("authors") {
    val id = integer("id")
    val name= varchar("name",255)
    var lastName= varchar("last_name", 255)
    override val primaryKey = PrimaryKey(id)
}