package ru.itmo.dkudaiberdieva.design.reactive.route

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.router
import ru.itmo.dkudaiberdieva.design.reactive.handler.HomeHandler
import ru.itmo.dkudaiberdieva.design.reactive.handler.UserHandler

@Configuration
class UserRoutes(private val homeHandler: HomeHandler, private val userHandler: UserHandler) {

    @Bean
    fun userRouter() = router {
        accept(TEXT_HTML).nest {
            GET("/", homeHandler::home)
            GET("/register", userHandler::registerView)
            GET("/login", userHandler::loginView)
            GET("/logout", userHandler::logout)
        }

        contentType(APPLICATION_FORM_URLENCODED).nest {
            POST("/register", userHandler::register)
            POST("/login", userHandler::login)
        }
    }
}