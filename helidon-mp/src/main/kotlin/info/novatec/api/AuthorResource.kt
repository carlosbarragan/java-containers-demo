package info.novatec.api

import info.novatec.adapters.db.Author
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/authors")
class AuthorResource {

    @PersistenceContext(unitName = "pu1")
    private lateinit var entityManager: EntityManager

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAuthors() : List<Author> = entityManager.createNamedQuery("getAuthors", Author::class.java).resultList
}