package novatec.info.model

import org.jetbrains.exposed.sql.Table

data class Author(
    val name: String,
    val lastName: String,
    val book: Book,
    val id: Long
)

object AuthorsTable: Table() {
    val id = long("id")
    val name= varchar("name",255)
    var lastName= varchar("lastName", 255)
    var book= long("book_id").references(BooksTable.id)
    override val primaryKey = PrimaryKey(id)
}