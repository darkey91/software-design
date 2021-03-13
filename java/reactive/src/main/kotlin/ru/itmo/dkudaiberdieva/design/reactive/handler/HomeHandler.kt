package ru.itmo.dkudaiberdieva.design.reactive.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import ru.itmo.dkudaiberdieva.design.reactive.entity.User
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency
import ru.itmo.dkudaiberdieva.design.reactive.repository.UserRepository
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayView

@Component
class HomeHandler(private val userRepository: UserRepository) {

    fun home(request: ServerRequest): Mono<ServerResponse> =
        request.session().flatMap { session ->
            session.attributes["id"]
                ?.let { userRepository.findById(it as Long) }
                ?.flatMap {
                    displayView("index", mapOf("username" to it.username!!))
                }
                ?: displayView("index")
        }
}
