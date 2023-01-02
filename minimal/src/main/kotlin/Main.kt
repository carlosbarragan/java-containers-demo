import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import java.util.logging.Logger

val logger = Logger.getLogger("main")

fun main(args: Array<String>) {
    val start = Instant.now()
    val properties = loadProperties()
    val connectionPool = configureConnectionPool(properties)
    val sqlClient = SimpleSqlClient(connectionPool)
    migrateSchemas(connectionPool.ds)
    initializeData(sqlClient)


    val authorHandler = AuthorsHandler(sqlClient)
    val httpServer = createHttpServer {
        createContext("/authors", authorHandler)
        executor = Executors.newVirtualThreadPerTaskExecutor()
    }
    httpServer.start()
    val duration = Duration.between(start, Instant.now())
    logger.info("Started in ${duration.toMillis()} ms. Listening on ${httpServer.address.port}")
}

fun loadProperties(): Properties = Properties().apply {
    Author::class.java.classLoader.getResourceAsStream("application.properties")?.use {
        load(it)
    }
}


class AuthorsHandler(val sqlClient: SimpleSqlClient) : HttpHandler {

    private val findAllBooksAndAuthors =
        "SELECT a.id as id, a.name as name, a.last_name as last_name, b.name as book_name, b.isbn as book_isbn FROM Authors as a join books as b on b.authors=a.id"

    override fun handle(exchange: HttpExchange) = exchange.use {

        val result = runCatching {

            sqlClient.queryList(findAllBooksAndAuthors) {

                val name = it.getString("name")
                val lastName = it.getString("last_name")
                val id = it.getLong("id")

                val bookName = it.getString("book_name")
                val isbn = it.getString("book_isbn")
                Author(name, lastName, Book(bookName, isbn, id), id)
            }
        }

        val authors = result.getOrElse {
            it.printStackTrace()
            listOf()
        }
        exchange.sendResponseAsJson(authors)


    }
}

fun initializeData(sqlClient: SimpleSqlClient) {
    (1..5).map {
        val authorId =
            sqlClient.execute("INSERT INTO Authors (name, last_name) VALUES (?, ?)", "name$it", "lastName$it")
        sqlClient.execute("INSERT INTO BOOKS(name, isbn, authors) VALUES(?,?,?)", "book$it", "isbn$it", authorId)
    }
}


class Author(val name: String, val lastName: String, val book: Book, var id: Long? = null)

class Book(val name: String, val isbn: String, var id: Long? = null)




