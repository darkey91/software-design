package ru.itmo.dkudaiberdieva.design.reactive.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import ru.itmo.dkudaiberdieva.design.reactive.entity.Product
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency
import ru.itmo.dkudaiberdieva.design.reactive.repository.ProductRepository
import ru.itmo.dkudaiberdieva.design.reactive.repository.UserRepository
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayErrorView
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayView
import java.lang.Long.parseLong

@Component
class ProductHandler(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {
    fun saveView(request: ServerRequest): Mono<ServerResponse> =
        displayView("saveProduct", mapOf("newProduct" to Product()))

    fun save(request: ServerRequest): Mono<ServerResponse> = request.formData().flatMap {
        it.toSingleValueMap().let { product ->
            request.session().flatMap { session ->
                val id = session.attributes["id"]!! as String
                userRepository.findById(id)
            }.switchIfEmpty(
                Mono.error { error("unregistered users can not add new products") }
            ).flatMap { user ->
                productRepository.save(
                    Product(
                        userId = user.id, name = product["name"], price = parseLong(product["price"]), currency =
                        Currency.valueOf(product["currency"]!!)
                    )
                )
            }.flatMap { ok().render("products") }
        }
    }.onErrorResume { e -> displayErrorView(e.cause.toString()) }


    //todo change currency
    fun listAll(request: ServerRequest): Mono<ServerResponse> =
        ok().render("products", mapOf("products" to productRepository.findAll()))
}