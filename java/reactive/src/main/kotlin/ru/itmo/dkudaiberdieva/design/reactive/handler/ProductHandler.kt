package ru.itmo.dkudaiberdieva.design.reactive.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.itmo.dkudaiberdieva.design.reactive.entity.Product
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency
import ru.itmo.dkudaiberdieva.design.reactive.repository.ProductRepository
import ru.itmo.dkudaiberdieva.design.reactive.repository.UserRepository
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayErrorView
import ru.itmo.dkudaiberdieva.design.reactive.utils.displayView
import java.lang.Double.parseDouble

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
                        userId = user.id, name = product["name"], price = parseDouble(product["price"]), currency =
                        Currency.valueOf(product["currency"]!!)
                    )
                )
            }.flatMap { ok().render("products")
            }.switchIfEmpty { error("Can not save product") }
        }
    }.onErrorResume { e -> displayErrorView(e.cause.toString()) }

    fun listAll(request: ServerRequest): Mono<ServerResponse> =
        request.session().flatMap { session ->
            val id = session.attributes["id"]!! as String
            userRepository.findById(id)
        }.flatMap { user ->
            val products = productRepository.findAll().map { product ->
                val preferredCurrency = user.currency!!
                val price = product.currency!!.convertToPreferredCurrency(product.price!!, preferredCurrency)
                Product(name = product.name, price = price, currency = preferredCurrency)
            }.collectList()

            displayView("products", mutableMapOf("products" to products, "username" to user.username!!))
        }.switchIfEmpty { error("Only authorized users can view product list")
        }.onErrorResume { e -> displayErrorView(e.cause.toString()) }
}