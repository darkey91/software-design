package ru.itmo.dkudaiberdieva.design.reactive.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.dkudaiberdieva.design.reactive.repository.UserRepository
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayView

@Component
class HomeHandler(private val userRepository: UserRepository) {

    fun home(request: ServerRequest): Mono<ServerResponse> =
        request.session().flatMap { session ->
            session.attributes["id"]
                ?.let { userRepository.findById(it as String) }
                ?.flatMap {
                    displayView("index", mapOf("username" to it.username!!))
                }?.switchIfEmpty { Mono.error { error("Can not find user") } }
                ?: displayView("index")
        }
}
