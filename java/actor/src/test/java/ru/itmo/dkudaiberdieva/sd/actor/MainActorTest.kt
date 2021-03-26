package ru.itmo.dkudaiberdieva.sd.actor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import com.fasterxml.jackson.databind.ObjectMapper
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchEngineResponse
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchRequest
import ru.itmo.dkudaiberdieva.sd.engine.ENGINES
import ru.itmo.dkudaiberdieva.sd.engine.GOOGLE
import ru.itmo.dkudaiberdieva.sd.engine.YANDEX
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainActorTest {
    private var system: ActorSystem? = null
    private val mapper = ObjectMapper()
    private val stubServers: MutableMap<String, StubServer?> =
        mutableMapOf(*ENGINES.map { it.name to null }.toTypedArray())

    @Before
    fun setUp() {
        system = ActorSystem.create("MainActorTest")

        ENGINES.forEach {
            val stubServer = StubServer(it.port).run()
            stubServers[it.name] = stubServer
        }
    }

    @After
    fun after() {
        system!!.terminate()
        ENGINES.forEach {
            stubServers[it.name]?.apply {
                stop()
            }
        }
    }

    @Test
    fun testMainActor() {
        ENGINES.forEach {
            val responseBody = mapOf("engine" to it.name, "result" to listOf("Hi!"))
            whenHttp(stubServers[it.name])
                .match(method(Method.GET))
                .then(
                    Action.stringContent(
                        mapper.writeValueAsString(responseBody)
                    )
                )
        }
        val responseConsumer = CompletableFuture<Map<String, List<String>>>()
        val mainActor: ActorRef =
            system!!.actorOf(Props.create(MainActor::class.java, responseConsumer), "mainTestActor")
        mainActor.tell(SearchRequest("query"), ActorRef.noSender())
        var response = responseConsumer.get()
        assertEquals(ENGINES.map { it.name }.toSet(), response.keys)
    }

    @Test
    fun testMainActorPartialTimeout() {
        ENGINES.forEach {
            if (it !== GOOGLE) {
                val responseBody = mapOf("engine" to it.name, "result" to listOf("Hi!"))
                whenHttp(stubServers[it.name])
                    .match(method(Method.GET))
                    .then(
                        Action.stringContent(
                            mapper.writeValueAsString(responseBody)
                        )
                    )
            }
        }
        val responseConsumer = CompletableFuture<Map<String, List<String>>>()
        val mainActor: ActorRef =
            system!!.actorOf(Props.create(MainActor::class.java, responseConsumer), "mainTestActor")
        mainActor.tell(SearchRequest("stickG"), ActorRef.noSender())
        val response = responseConsumer.get()

        assertNull(response[GOOGLE.name])
        assertEquals(response.size, ENGINES.size - 1)
    }

    @Test
    fun testMainActorTimeout() {

        val responseConsumer = CompletableFuture<Map<String, List<String>>>()
        val mainActor: ActorRef =
            system!!.actorOf(Props.create(MainActor::class.java, responseConsumer), "mainTestActor")
        mainActor.tell(SearchRequest("stick"), ActorRef.noSender())
        val response = responseConsumer.get()

        assertTrue(response.isEmpty())
    }
}