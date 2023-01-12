package info.novatec.adapters.db

import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.transaction.Transactional
import kotlin.random.Random

@ApplicationScoped
class DataImporter(
    val bookRepository: BookRepository,
    val authorRepository: AuthorRepository
) {
    @Transactional
    fun onStart(@Observes ev: StartupEvent) {
        println("Saving")
        (1..5)
            .map {
                val book = Book(
                    name = "title$it",
                    isbn = randomString()
                )
                bookRepository.persist(book)

                Author(
                    name = "name$it",
                    lastName = "lastName$it",
                    book = book
                )

            }
            .forEach { authorRepository.persist(it) }
    }
}

val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght: Int = 13): String =
    (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")