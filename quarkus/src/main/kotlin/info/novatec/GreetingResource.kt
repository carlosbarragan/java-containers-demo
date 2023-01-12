package info.novatec

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import org.jboss.logging.Logger
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlin.random.Random

@Path("/authors")
class GreetingResource(
    val authorRepository: AuthorRepository
) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello() = authorRepository.findAll().list()
}

@ApplicationScoped
class DataImporter(
    val authorRepository: AuthorRepository,
    val bookRepository: BookRepository
) {

    private val log = Logger.getLogger(DataImporter::class.java)

    @Transactional
    fun onStart(@Observes ev: StartupEvent) {
        log.info("Saving")
        (1..5)
            .map {
                val book=Book(
                    name="title$it",
                    isbn= randomString()
                )
                bookRepository.persist(book)

                Author(
                    name="name$it",
                    lastName="lastName$it",
                    book= book
                )

            }
            .forEach { authorRepository.persist(it) }
    }
}



@ApplicationScoped
class AuthorRepository: PanacheRepository<Author>

@ApplicationScoped
class BookRepository: PanacheRepository<Book>

@Entity
@DefaultNoArgConstructor
data class Author (
    var name:String,
    var lastName:String,
    @OneToOne
    var book: Book,
    @Id @GeneratedValue
    val id:Long?=null
)

@Entity
@DefaultNoArgConstructor
data class Book (
    var name:String,
    var isbn:String,
    @Id
    @GeneratedValue
    val id:Long? =null
)

val charPool : List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght:Int=13): String = (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")