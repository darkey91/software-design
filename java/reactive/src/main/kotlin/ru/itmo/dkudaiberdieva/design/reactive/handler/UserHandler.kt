package ru.itmo.dkudaiberdieva.design.reactive.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import reactor.kotlin.adapter.rxjava.toSingle
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import ru.itmo.dkudaiberdieva.design.reactive.entity.User
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency
import ru.itmo.dkudaiberdieva.design.reactive.repository.UserRepository
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayErrorView
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayView

@Component
class UserHandler(private val userRepository: UserRepository) {
    fun registerView(request: ServerRequest): Mono<ServerResponse> = displayView("register", mapOf("newUser" to User()))

    fun register(request: ServerRequest): Mono<ServerResponse> = request.formData().flatMap {
        it.toSingleValueMap().let { user ->
            val username = user["username"] ?: error("Username must not be null")
            val name = user["name"] ?: error("Name must not be null")
            userRepository.findByUsername(username)
                .flatMap { Mono.error<User> { error("User with username $username already exists") } }
                .switchIfEmpty(
                    Mono.defer {
                        userRepository.save(
                            User(username = username, name = name, currency = Currency.valueOf(user["currency"]!!))
                        )
                    }
                )
            ok().render("index")
        }
    }.onErrorResume { e -> displayErrorView(e.message.toString()) }

    fun loginView(request: ServerRequest): Mono<ServerResponse> = displayView("login", mapOf("user" to User()))

    fun login(request: ServerRequest): Mono<ServerResponse> = request.formData().flatMap {
        it.toSingleValueMap().let { formData ->
            val username = formData["username"] ?: error("Username must not be null")
            userRepository.findByUsername(username)
                .switchIfEmpty { Mono.error<User> { error("Wrong username") } }
        }.flatMap { user ->
            request.session().flatMap { session ->
                session.apply {
                    attributes["id"] = user.id
                }
                ok().render("index", mapOf("username" to user.username))
            }
        }.onErrorResume { e -> displayErrorView(e.message.toString()) }
    }

    fun logout(request: ServerRequest): Mono<ServerResponse> =
        request.session().flatMap {
            clearSession(it)
            displayView("index")
        }

    private fun clearSession(session: WebSession) {
        session.attributes.remove("id")
    }
}