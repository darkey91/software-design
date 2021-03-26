package ru.itmo.dkudaiberdieva.sd.server;

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.itmo.dkudaiberdieva.sd.engine.Engine;

class HttpHandler(private val engine: Engine) : HttpHandler {

    private val mapper = ObjectMapper()

    companion object {
        const val STICK = "stick"
        const val STICK_GOOGLE = "stickG"
        const val STICK_YANDEX = "stickY"
        const val STICK_BING = "stickB"
        const val STICK_TIME_MS = 5000L
        const val BAD = "bad"
        val STICKS =   listOf(STICK_GOOGLE, STICK_YANDEX, STICK_BING)
    }

    override fun handle(exchange: HttpExchange) {
        var query = exchange.requestURI.query

        when {
            exchange.requestMethod != "GET" -> {
                sendError(exchange, "Only 'GET' requests are supported.")
                return
            }
            !query.startsWith("q=") -> {
                sendError(exchange, "Wrong URI format. It should contain '?q={your query}'")
                return
            }
        }
        query = query.takeLast(query.length - 2)

        when {
            STICK == query -> Thread.sleep(STICK_TIME_MS)
            STICKS.contains(query) && (query.last() == engine.name.firstOrNull()) -> {
                Thread.sleep(STICK_TIME_MS)
            }
            query == BAD -> {
                sendError(exchange, "Bad request.")
                return
            }
        }

        val results = IntRange(0, 4)
            .map { "Advice #$it: Don't worry, be happy!" }
            .toList()

        val responseBody = mapper.writeValueAsString(mapOf("engine" to engine.name, "result" to results))
        exchange.sendResponseHeaders(200, responseBody.length.toLong())
        exchange.responseBody.use {
            it.write(responseBody.toByteArray())
            it.flush()
        }
    }


    private fun sendError(exchange: HttpExchange, reason: String) {


    }
}
