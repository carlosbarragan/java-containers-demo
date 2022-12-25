import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress


/**
 * This file contains some utility and extension functions to streamline the API of the HttpServer
 * and interaction with other web components like request, response and json serialization.
 */


fun createHttpServer(config: HttpServer.() -> Unit): HttpServer {
    val httpServer = HttpServer.create(InetSocketAddress(8080), 0)
    httpServer.config()
    return httpServer
}

fun <T> HttpExchange.sendResponseAsJson(response: T, responseCode: Int = 200) {
    responseHeaders["Content-Type"] = "application/json"
    val responseBytes = objectMapper.writeValueAsBytes(response)
    sendResponseHeaders(responseCode, responseBytes.size.toLong())
    responseBody.use { it.write(responseBytes) }
}

val objectMapper = jacksonObjectMapper()