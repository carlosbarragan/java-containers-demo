package novatec.info.model

import org.jetbrains.exposed.sql.Table

data class Book(
    val name:String,
    val isbn:String,
    val id:Int
)

object BooksTable: Table("books") {
    val id = integer("id")
    val name=varchar("name",255)
    var isbn=varchar("isbn", 255)
    var authors = integer("authors").references(AuthorsTable.id)
    override val primaryKey = PrimaryKey(id)
}
