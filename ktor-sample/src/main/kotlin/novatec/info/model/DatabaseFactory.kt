package novatec.info.model

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import kotlin.random.Random


object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        transaction {
            create(BooksTable, AuthorsTable)

            (1..5).map { counter ->
                BooksTable.insert {
                    it[name] = "title$counter"
                    it[isbn] = randomString()
                    it[id] = counter.toLong()
                }

                AuthorsTable.insert {
                    it[name] = "name$counter"
                    it[lastName] = "lastName$counter"
                    it[book] = counter.toLong()
                    it[id] = counter.toLong()
                }
            }
        }
    }
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.validate()
        return HikariDataSource(config)
    }

    private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

    private fun randomString(lenght: Int = 13): String =
        (1..lenght).map { Random.nextInt(0, charPool.size) }.map { charPool[it] }.joinToString("")
}