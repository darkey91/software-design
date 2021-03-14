package ru.itmo.dkudaiberdieva.design.reactive.route

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.router
import ru.itmo.dkudaiberdieva.design.reactive.handler.ProductHandler

@Configuration
class ProductRoutes(private val productHandler: ProductHandler) {

    @Bean
    fun productRouter() = router {

        accept(TEXT_HTML).nest {
            GET("/products", productHandler::listAll)
            GET("/save/product", productHandler::saveView)
        }

        contentType(APPLICATION_FORM_URLENCODED).nest {
            POST("/save/product", productHandler::save)
        }
    }
}