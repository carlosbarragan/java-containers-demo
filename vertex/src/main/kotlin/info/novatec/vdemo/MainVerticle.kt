package info.novatec.vdemo

import io.vertx.core.Launcher
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.jdbc.spi.DataSourceProvider
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.jdbcclient.JDBCPool
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.launch
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.flywaydb.core.api.output.MigrateResult
import java.time.Duration
import java.time.Instant
import java.util.logging.Logger

class MainVerticle : CoroutineVerticle() {

  val logger = Logger.getLogger(MainVerticle::class.java.name)

  override suspend fun start() {
    val start = Instant.now()
    val router = Router.router(vertx)
    val dataSourceProvider = createDataSourceProvider()
    val pool = vertx.createPool(dataSourceProvider)
    vertx.migrateData(dataSourceProvider)
    createInitialData(pool)


    router.get("/authors").coroutineHandler {
      it.response()
        .putHeader("Content-Type", "application/json")
        .end(Json.encode(retrieveAllAuthors(pool))).await()
    }

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080).onComplete {
        val duration = Duration.between(start, Instant.now()).toMillis()
        logger.info("Server started in $duration ms")
      }.await()
  }

  // https://github.com/vert-x3/vertx-lang-kotlin/issues/194
  fun Route.coroutineHandler(operation: suspend (RoutingContext) -> Unit) {
    handler {
      launch(it.vertx().dispatcher()) {
        try {
          operation(it)
        } catch (e: Exception) {
          it.fail(e)
        }
      }
    }
  }
}


fun Vertx.createPool(dataSourceProvider: DataSourceProvider): JDBCPool {
  return JDBCPool.pool(this, dataSourceProvider)
}

const val FIND_ALL_AUTHORS_AND_BOOKS =
  "SELECT a.id as id, a.name as name, a.last_name as last_name, b.name as book_name, b.isbn as book_isbn FROM Authors as a join books as b on b.authors=a.id"

class Author(val name: String, val lastName: String, val book: Book, var id: Long? = null)

class Book(val name: String, val isbn: String, var id: Long? = null)

suspend fun retrieveAllAuthors(pool: JDBCPool): List<Author> {
  val authors = pool.preparedQuery(FIND_ALL_AUTHORS_AND_BOOKS).mapping {

    val name = it.getString("NAME")
    val lastName = it.getString("LAST_NAME")
    val id = it.getLong("ID")

    val bookName = it.getString("BOOK_NAME")
    val isbn = it.getString("BOOK_ISBN")
    Author(name, lastName, Book(bookName, isbn, id), id)
  }.execute().await()
  return authors.toList()
}

fun createDataSourceProvider(): DataSourceProvider {
  val config =
    jsonObjectOf(
      "jdbcUrl" to "jdbc:h2:mem:test;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE",
      "datasourceName" to "h2-datasource",
      "username" to "sa",
      "maximumPoolSize" to 50,
      "provider_class" to "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider"
    )
  return DataSourceProvider.create(config)
}

suspend fun Vertx.migrateData(dataSourceProvider: DataSourceProvider) {
  val config = ClassicConfiguration().apply {
    dataSource = dataSourceProvider.getDataSource(dataSourceProvider.initialConfig)
  }

  val flyway = Flyway(config)
  executeBlocking<MigrateResult> {
    it.complete(flyway.migrate())
  }.await()


}

suspend fun createInitialData(pool: JDBCPool) {
  (1..5).map {
    val authorId =
      pool.preparedQuery("INSERT INTO Authors (name, last_name) VALUES (?, ?)")
        .execute(Tuple.of("name$it", "lastName$it")).await()
        .property(JDBCPool.GENERATED_KEYS).getLong(0)

    pool.preparedQuery("INSERT INTO BOOKS(name, isbn, authors) VALUES(?,?,?)")
      .execute(Tuple.of("book$it", "isbn$it", authorId)).await()

  }
}

fun main() {
  Launcher.executeCommand("run", MainVerticle::class.java.name)
}
