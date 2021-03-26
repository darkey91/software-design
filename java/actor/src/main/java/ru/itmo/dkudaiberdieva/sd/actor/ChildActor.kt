package ru.itmo.dkudaiberdieva.sd.actor

import akka.actor.AbstractActor
import akka.http.javadsl.Http
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.HttpResponse
import akka.japi.pf.ReceiveBuilder
import akka.pattern.Patterns.pipe
import akka.stream.javadsl.Sink
import akka.util.ByteString
import com.fasterxml.jackson.databind.ObjectMapper
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchEngineRequest
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchEngineResponse
import java.util.concurrent.CompletionStage
import scala.concurrent.ExecutionContextExecutor
import akka.event.Logging

import akka.event.LoggingAdapter

class ChildActor : AbstractActor() {
    private val http: Http = Http.get(context().system())
    private val mapper = ObjectMapper()
    private val log = Logging.getLogger(context.system, this)

    override fun createReceive(): Receive = ReceiveBuilder()
        .match(String::class.java) { url -> sendRequest(url) }
        .build()

    private fun sendRequest(url: String) {
        log.debug("Send request: $url")
        val sender = sender
        http.singleRequest(HttpRequest.create(url))
            .thenCompose { res ->
                res.entity().dataBytes
                    .runFold("", { agg, next: ByteString -> agg + next.utf8String() }, context.system)
                    .thenApply { mapper.readValue(it, SearchEngineResponse::class.java) }
            }.handle { res, e ->
                if (res != null) {
                    log.debug("Response received :$res")
                    sender.tell(res, self)
                } else {
                    log.error(e.toString())
                }
                context.stop(self)
            }
    }
}