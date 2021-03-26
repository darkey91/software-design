package ru.itmo.dkudaiberdieva.sd.server

import ru.itmo.dkudaiberdieva.sd.engine.ENGINES
import java.io.IOException
import java.util.*


fun main() {
    val servers = ArrayList<Server>()

    for (engine in ENGINES) {
        try {
            servers.add(Server(engine))
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
    }
    try {
        servers.forEach(Server::start)
        while (true) { }
    } finally {
        servers.forEach { dummyServer ->
            try {
                dummyServer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
