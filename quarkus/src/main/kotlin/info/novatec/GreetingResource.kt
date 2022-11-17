package info.novatec

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
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
class GreetingResource {

    @Inject
    private lateinit var authorRepository: AuthorRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello() = authorRepository.findAll().list()
}

@ApplicationScoped
class DataImporter {

    @Inject
    lateinit var authorRepository: AuthorRepository

    @Inject
    lateinit var bookRepository: BookRepository

    @Transactional
    fun onStart(@Observes ev: StartupEvent) {
        println("Saving")
        (1..5)
            .map {
                val book=Book().apply {
                    name="title$it"
                    isbn= randomString()
                }
                bookRepository.persist(book)

                Author().apply {
                    name="name$it"
                    lastName="lastName$it"
                    this.book= book
                }

            }
            .forEach { authorRepository.persist(it) }
    }

    }



@ApplicationScoped
class AuthorRepository: PanacheRepository<Author>

@ApplicationScoped
class BookRepository: PanacheRepository<Book>

@Entity
class Author {
    lateinit var name:String
    lateinit var lastName:String
    @OneToOne
    lateinit var book: Book
    @Id @GeneratedValue var id:Long?=null }

@Entity
class Book {
    lateinit var name:String
    lateinit var isbn:String
    @Id
    @GeneratedValue
    var id:Long? =null
}

val charPool : List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght:Int=13): String = (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")