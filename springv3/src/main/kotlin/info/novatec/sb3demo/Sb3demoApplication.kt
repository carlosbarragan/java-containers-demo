package info.novatec.sb3demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@SpringBootApplication
class Sb3demoApplication {

	@Bean
	fun initData(authorRepository: AuthorRepository): CommandLineRunner = CommandLineRunner {
		(1..5)
			.map {
				val book=Book(name="title$it", isbn = randomString())
				Author(name = "name$it", lastName = "lastName$it",book=book)
			}
			.forEach { authorRepository.save(it) }
	}

}

fun main(args: Array<String>) {
	runApplication<Sb3demoApplication>(*args)
}

@RestController
class AuthorController(private val authorRepository: AuthorRepository) {

	@GetMapping("authors", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun getAllAuthors():List<Author> = authorRepository.findAll().toList()

}

interface AuthorRepository: CrudRepository<Author, Long>


@Table("AUTHORS")
class Author(val name:String, val lastName:String, val book: Book, @Id var id:Long?=null)

@Table("BOOKS")
class Book(val name:String, val isbn:String, @Id var id:Long? =null)

val charPool : List<Char> = ('A'..'Z') + ('0'..'9')

fun randomString(lenght:Int=13): String = (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")