package info.novatec.srdemo

import com.fasterxml.jackson.annotation.JsonIgnore
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.MediaType
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.random.Random

@SpringBootApplication
class SpringReactiveApplication {

	@Bean
	fun dataInitializer(connectionFactory:ConnectionFactory):ConnectionFactoryInitializer {
		val populator = ResourceDatabasePopulator().apply {
			addScript(ClassPathResource("db/migration/V1_1_0__initial_schema.sql"))
		}

		return ConnectionFactoryInitializer().apply {
			setConnectionFactory(connectionFactory)
			setDatabasePopulator(populator)
		}

	}

	@Bean
	fun initData(authorRepository: AuthorRepository, bookRepository: BookRepository): CommandLineRunner = CommandLineRunner {

		val authors=(1..5)
			.map {

				Author(name = "name$it", lastName = "lastName$it")
			}

		Flux.fromIterable(authors)
			.flatMap {
				authorRepository.save(it)

			}.flatMap {
				bookRepository.save(Book(name="title_from${it.name}", isbn = randomString(), authors = it.id))
			}
		.blockLast(Duration.ofSeconds(5))


	}

}

fun main(args: Array<String>) {
	runApplication<SpringReactiveApplication>(*args)
}



@RestController
class AuthorController(private val authorRepository: AuthorFetcher) {

	@GetMapping("authors", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun getAllAuthors():Flux<Author> = authorRepository.findAuthorsAndBooks()

}

@Service
class AuthorFetcher(private val databaseClient: DatabaseClient){

	fun findAuthorsAndBooks(): Flux<Author> {
		return databaseClient.sql("SELECT a.id as author_id, a.name as author_name, a.last_name as author_lastname, b.name as book_name, b.isbn as book_isbn, b.id as book_id FROM AUTHORS as a JOIN Books as b on b.authors=a.id")
			.map { row, _ ->
				val bookName = row.getValue<String>("book_name")
				val isbn = row.getValue<String>("book_isbn")
				val bookId = row.getValue<Int>("book_id")

				val book = Book(bookName,isbn,bookId.toLong())

				val authorName = row.getValue<String>("author_name")
				val lastName = row.getValue<String>("author_lastname")
				val authorId = row.getValue<Int>("author_id")
				Author(authorName, lastName,authorId.toLong(),book)
			}.all()
	}


}

inline fun <reified T> Row.getValue(columnName:String):T = get(columnName, T::class.java) as T


interface AuthorRepository: ReactiveCrudRepository<Author,Long>
interface BookRepository:ReactiveCrudRepository<Book, Long>

@Table("AUTHORS")
class Author(val name:String, val lastName:String, @Id var id:Long?=null, val book: Book? =null)

@Table("BOOKS")
class Book(val name:String, val isbn:String, @Id var id:Long? =null, @JsonIgnore val authors: Long? = null)


val charPool : List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght:Int=13): String = (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")