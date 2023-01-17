package novatec.info.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class AuthorRepository {

    suspend fun getAllAuthors(): List<Author> = newSuspendedTransaction {
        (AuthorsTable innerJoin BooksTable).selectAll().map { toAuthor(it) }
    }

    private fun toAuthor(row: ResultRow) = Author(
        id = row[AuthorsTable.id],
        name = row[AuthorsTable.name],
        lastName = row[AuthorsTable.lastName],
        book = Book(
            id = row[BooksTable.id],
            name = row[BooksTable.name],
            isbn = row[BooksTable.isbn]
        )
    )
}