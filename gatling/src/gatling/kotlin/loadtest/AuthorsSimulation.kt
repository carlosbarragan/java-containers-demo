package loadtest

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*


class AuthorsSimulation : Simulation() {

    val parallelUsers = Integer.getInteger("parallelUsers", 1000)
    val numberOfRequests = Integer.getInteger("numberOfRequests", 100)

    val getAuthors = repeat(numberOfRequests).on(exec(
        http("Authors")
            .get("/authors")
            .check(status().shouldBe(200))
    ))

    val httpProtocol =
        http.baseUrl("http://localhost:8080")

    val users = scenario("Users").exec(getAuthors)

    init {
        setUp(
            users.injectOpen(rampUsers(parallelUsers).during(10))
        ).protocols(httpProtocol)
    }
}