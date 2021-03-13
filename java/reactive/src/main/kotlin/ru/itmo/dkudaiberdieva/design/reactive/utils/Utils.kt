package ru.itmo.dkudaiberdieva.design.reactive.utils

import org.springframework.web.reactive.function.server.ServerResponse


fun displayView(page: String, context: Map<String, Any> = emptyMap()) = ServerResponse.ok().render(page, context)

fun displayErrorView(errMsg: String) = displayView("error", mapOf("errorMessage" to errMsg))