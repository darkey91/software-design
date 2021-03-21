package ru.itmo.dkudaiberdieva.sd.actor.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SearchEngineResponse(@JsonProperty("engine") val engine: String, @JsonProperty("result") val result: List<String>)
