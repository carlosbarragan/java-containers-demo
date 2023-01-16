package info.novatec.adapters.db

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.Initialized
import jakarta.enterprise.event.Observes
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import java.util.logging.Logger
import kotlin.random.Random

@ApplicationScoped
class DataImporter {

    private val log: Logger = Logger.getLogger(DataImporter::class.java.name)

    @PersistenceContext(unitName = "pu1")
    private lateinit var entityManager: EntityManager

    @Transactional
    fun onStartup(@Observes @Initialized(ApplicationScoped::class) event: Any) {
        log.info("Saving")
        (1..5)
            .map {
                val book = Book(
                    name = "title$it",
                    isbn = randomString()
                )
                entityManager.persist(book)

                Author(
                    name = "name$it",
                    lastName = "lastName$it",
                    book = book
                )

            }
            .forEach { entityManager.persist(it) }
    }

    val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

    fun randomString(lenght: Int = 13): String =
        (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")
}