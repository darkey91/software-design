package ru.itmo.dkudaiberdieva.sd.actor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.PatternsCS.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.ObjectMapper
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.itmo.dkudaiberdieva.sd.actor.dto.SearchEngineResponse
import ru.itmo.dkudaiberdieva.sd.engine.ENGINES
import ru.itmo.dkudaiberdieva.sd.engine.GOOGLE
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals


class ChildActorTest {
    private var system: ActorSystem? = null
    private val mapper = ObjectMapper()
    private val stubServers: MutableMap<String, StubServer?> =
        mutableMapOf(*ENGINES.map { it.name to null }.toTypedArray())

    @Before
    fun setUp() {
        system = ActorSystem.create("ChildActorTest")

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
    fun testChildActor() {
        val engine = GOOGLE
        val expectedResult = listOf("Hi!")
        val responseBody = mapOf("engine" to engine.name, "result" to listOf("Hi!"))
        whenHttp(stubServers[engine.name])
            .match(method(Method.GET))
            .then(
                Action.stringContent(
                    mapper.writeValueAsString(responseBody)
                )
            )

        val url = "http://localhost:" + GOOGLE.port + "?q=dog"
        val childActor: ActorRef = system!!
            .actorOf(Props.create(ChildActor::class.java))

        val response = ask(childActor, url, Timeout.apply(10, TimeUnit.SECONDS))
            .toCompletableFuture().join() as SearchEngineResponse

        assertEquals(GOOGLE.name, response.engine)
        assertEquals(expectedResult, response.result)
    }

}