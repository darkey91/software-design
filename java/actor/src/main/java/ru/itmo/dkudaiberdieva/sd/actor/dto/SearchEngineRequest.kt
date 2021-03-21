package ru.itmo.dkudaiberdieva.sd.actor.dto

import ru.itmo.dkudaiberdieva.sd.engine.Engine


data class SearchEngineRequest(val host: String, val port: Int, val query: String) {
    constructor(engine: Engine, query: String): this(engine.host, engine.port, query)
}
