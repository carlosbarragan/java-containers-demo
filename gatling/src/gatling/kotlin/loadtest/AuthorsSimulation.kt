package loadtest

import io.gatling.javaapi.core.*
import io.gatling.javaapi.http.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*

import java.util.concurrent.ThreadLocalRandom

class AuthorsSimulation : Simulation() {

    val getAuthors = repeat(10000).on(exec(
        http("Authors")
            .get("/authors")
            .check(status().shouldBe(200))
    ))

    val httpProtocol =
        http.baseUrl("http://localhost:8080")

    val users = scenario("Users").exec(getAuthors)

    init {
        setUp(
            users.injectOpen(rampUsers(10).during(0))
        ).protocols(httpProtocol)
    }
}