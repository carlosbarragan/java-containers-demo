package info.novatec.adapters.db

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthorRepository : PanacheRepository<Author>