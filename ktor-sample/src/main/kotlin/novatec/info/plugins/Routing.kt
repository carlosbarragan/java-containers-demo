package novatec.info.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import novatec.info.model.AuthorRepository

fun Application.configureRouting(authorRepository: AuthorRepository) {
    routing {
        get("/authors") {
            val authors = authorRepository.getAllAuthors()
            println(authors)
            call.respond(authors)
        }
    }
}
