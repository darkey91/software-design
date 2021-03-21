package ru.itmo.dkudaiberdieva.sd.server

import ru.itmo.dkudaiberdieva.sd.engine.ENGINES
import ru.itmo.dkudaiberdieva.sd.engine.Engine

import java.io.IOException
import java.lang.Exception
import java.util.ArrayList



fun main() {
    val servers = ArrayList<DummyServer>()

    for (engine in ENGINES) {
        try {
            servers.add(DummyServer(engine))
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
    }
    try {
        servers.forEach(DummyServer::start)
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
