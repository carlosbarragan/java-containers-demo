package novatec.info

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import novatec.info.model.AuthorRepository
import novatec.info.model.DatabaseFactory
import novatec.info.plugins.configureRouting

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    install(ContentNegotiation) {
        jackson()
    }
    configureRouting(AuthorRepository())
}
