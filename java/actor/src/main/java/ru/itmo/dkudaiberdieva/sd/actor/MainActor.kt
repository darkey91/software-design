package ru.itmo.dkudaiberdieva.sd.actor

import akka.actor.AbstractActor
import akka.actor.Props
import akka.actor.ReceiveTimeout
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchEngineResponse
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchRequest
import ru.itmo.dkudaiberdieva.sd.engine.ENGINES
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture

class MainActor(private val resultConsumer: CompletableFuture<Map<String, List<String>>>) : AbstractActor() {

    companion object {
        val TIMEOUT = Duration.of(5000L + 100L, ChronoUnit.MILLIS)
    }

    private var awaitingResultAmount = ENGINES.size
    private val result = HashMap<String, List<String>>()

    override fun createReceive(): Receive =
        receiveBuilder()
            .match(SearchRequest::class.java) { req -> this.sendRequest(req) }
            .match(SearchEngineResponse::class.java) { res -> retrieveResponse(res) }
            .match(ReceiveTimeout::class.java) { sendResult() }
            .build()


    private fun sendRequest(request: SearchRequest) {
        ENGINES.forEach { engine ->
            val url = "http://${engine.host}:${engine.port}?q=${request.query}"
            val engineActorName = "${engine.name}_actor"
            val engineActor = context.actorOf(Props.create(ChildActor::class.java), engineActorName)
            engineActor.tell(url, self)
        }

        context.receiveTimeout = TIMEOUT
    }

    private fun retrieveResponse(response: SearchEngineResponse) {
        result[response.engine] = response.result

        if (awaitingResultAmount == result.size) {
            context.cancelReceiveTimeout()
            sendResult()
            context.stop(self)
        }
    }

    private fun sendResult() {
        resultConsumer.complete(result)
        context.stop(self)
    }
}