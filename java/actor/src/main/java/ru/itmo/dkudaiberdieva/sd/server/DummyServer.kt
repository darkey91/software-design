package ru.itmo.dkudaiberdieva.sd.server

import com.sun.net.httpserver.HttpServer
import ru.itmo.dkudaiberdieva.sd.engine.Engine

import java.net.InetSocketAddress

class DummyServer(private val engine: Engine) : AutoCloseable {
    private val server: HttpServer = HttpServer.create(InetSocketAddress(engine.host, engine.port), 0)

    fun start() {
        server.createContext("/", DummyHttpHandler(engine))
        server.start()
    }

    override fun close() {
        server.stop(0)
    }
}
