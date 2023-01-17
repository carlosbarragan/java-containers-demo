package novatec.info.model

import org.jetbrains.exposed.sql.Table

data class Book(
    val name:String,
    val isbn:String,
    val id:Long
)

object BooksTable: Table() {
    val id = long("id")
    val name=varchar("name",255)
    var isbn=varchar("isbn", 255)
    override val primaryKey = PrimaryKey(id)
}
