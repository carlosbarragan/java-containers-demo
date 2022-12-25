import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import javax.sql.DataSource

/**
 * A simple, non-ambitious SQL Client.
 */
class SimpleSqlClient(private val pool: ConnectionPool) {

    fun execute(sql: String, vararg values: Any): Long = pool.getConnection().use {
        val statement = it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        statement.setValues(* values)
        val executionResult = statement.use { st ->
            st.executeUpdate()
            st.generatedKeys.use { genKeyRs -> if (genKeyRs.next()) genKeyRs.getLong(1) else -1 }
        }

        executionResult

    }


    fun <T> queryList(sql: String, mapper: RowMapper<T>): List<T> = pool.getConnection().use { conn ->
        val statement = conn.prepareStatement(sql)
        statement.use {
            val resultSet = statement.executeQuery()
            val results = mutableListOf<T>()
            resultSet.use { rs ->
                while (rs.next()) {
                    results += mapper(rs)
                }

            }
            results
        }

    }

}


fun PreparedStatement.setValues(vararg values: Any) {
    values.forEachIndexed { index, value ->
        val paramIndex = index + 1 //Lovely vintage SQL API
        setValue(paramIndex, value)
    }
}

fun PreparedStatement.setValue(index: Int, value: Any) {
    when (value) {
        is String -> setString(index, value)
        is Int -> setInt(index, value)
        is Long -> setLong(index, value)
        is Boolean -> setBoolean(index, value)
        else -> throw IllegalStateException("I was too lazy to handle the value of ${value::class}")
    }

}

class ConnectionPool(val ds: DataSource) {

    fun getConnection(): Connection = ds.connection
}

fun migrateSchemas(ds: DataSource) {
    logger.info("starting schema migration")
    val configuration = ClassicConfiguration().apply {
        dataSource = ds

    }

    val flyway = Flyway(configuration)
    val result = flyway.migrate()
    if (result.success) {
        logger.info("DB migrated successfully")
    }
}

fun configureConnectionPool(properties: Properties): ConnectionPool {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl =
            properties.getProperty("db.url") ?: throw IllegalStateException("db.url not found in properties")
        username = properties.getProperty("db.user", "sa")
    }

    return ConnectionPool(HikariDataSource(hikariConfig))

}

typealias RowMapper<T> = (ResultSet) -> T