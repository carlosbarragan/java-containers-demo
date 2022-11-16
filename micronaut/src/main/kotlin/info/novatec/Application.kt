package info.novatec

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Join
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.micronaut.discovery.event.ServiceReadyEvent
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.runtime.Micronaut.*
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import kotlin.random.Random

fun main(args: Array<String>) {
	run(*args)
}

@Singleton
class InitData(private val authorRepository: AuthorRepository,
private val bookRepository: BookRepository) {

	@EventListener
	fun init(event: ServiceReadyEvent) {
		(1..5)
			.map {
				val book=Book(name="title$it", isbn = randomString())
				val savedBook=bookRepository.save(book)
				Author(name = "name$it", lastName = "lastName$it",book=savedBook)
			}
			.forEach { authorRepository.save(it) }
	}
}


@Controller("/authors")
@Produces(MediaType.APPLICATION_JSON)
class AuthorController(private val authorRepository: AuthorRepository) {

	@Get
	fun getAllAuthors() = authorRepository.list()
}

@MappedEntity
class Author(val name:String,
			 val lastName:String,
			 @field:Relation(Relation.Kind.ONE_TO_ONE) val book: Book,
			 @field:Id @field:GeneratedValue var id:Long?=null)

@MappedEntity
class Book(val name:String, val isbn:String, @field:Id @field:GeneratedValue var id:Long? =null)

@JdbcRepository(dialect = Dialect.H2)
interface AuthorRepository : CrudRepository<Author, Long> {

	@Join(value = "book", type = Join.Type.FETCH)
	fun list():List<Author>
}

@JdbcRepository(dialect = Dialect.H2)
interface BookRepository : CrudRepository<Book, Long>


val charPool : List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght:Int=13): String = (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")