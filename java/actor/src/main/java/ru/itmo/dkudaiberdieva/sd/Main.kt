package ru.itmo.dkudaiberdieva.sd

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ActorSystem
import ru.itmo.dkudaiberdieva.sd.actor.MainActor
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun main() {
    val system = ActorSystem.create("ActorSystem")

    while (true) {
        val query = readLine()!!

        val resultConsumer = CompletableFuture<Map<String, List<String>>>()
        val actor = system.actorOf(Props.create(MainActor::class.java, resultConsumer), "mainActor")
        actor.tell(SearchRequest(query), ActorRef.noSender())
        val result = resultConsumer.get(8, TimeUnit.SECONDS)
        println(result)
    }
}