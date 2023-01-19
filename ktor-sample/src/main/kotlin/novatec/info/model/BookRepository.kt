package novatec.info.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class BookRepository {

    suspend fun getBookById(booksId: Int): Book = newSuspendedTransaction {
        BooksTable.select { BooksTable.id eq booksId }.map { toBook(it) }.first()
    }
    
    private fun toBook(row: ResultRow) = Book(
        id = row[BooksTable.id],
        name = row[BooksTable.name],
        isbn = row[BooksTable.isbn]
    )
}