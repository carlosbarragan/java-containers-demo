package novatec.info.model

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random


object DatabaseFactory {
    private val logger: Logger = LoggerFactory.getLogger(DatabaseFactory::class.java)
    fun init() {
        Database.connect(Hikari.ds)
        transaction {
            logger.info("starting schema migration")
            val configuration = ClassicConfiguration().apply {
                dataSource = Hikari.ds

            }
            val flyway = Flyway(configuration)
            val result = flyway.migrate()
            if (result.success) {
                logger.info("DB migrated successfully")
            }
        }

        transaction {
            logger.info("saving books and authors")
            (1..5).map { counter ->
                AuthorsTable.insert {
                    it[name] = "name$counter"
                    it[lastName] = "lastName$counter"
                    it[id] = counter
                }

                BooksTable.insert {
                    it[name] = "title$counter"
                    it[isbn] = randomString()
                    it[authors] = counter
                    it[id] = counter
                }

            }
        }
    }

    private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

    private fun randomString(lenght: Int = 13): String =
        (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")
}

object Hikari {
    val ds = HikariDataSource(HikariConfig().apply {
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:mem:test"
        validate()
    })
}