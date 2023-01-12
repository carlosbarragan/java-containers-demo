package info.novatec

import info.novatec.adapters.db.AuthorRepository
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
class GreetingResource(val authorRepository: AuthorRepository) {

    @GET
    @Transactional
    fun authors() = authorRepository.findAll().list()
}