package ru.itmo.dkudaiberdieva.design.reactive.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver

@Configuration
class WebFluxConfig(private val thymeleafReactiveViewResolver: ThymeleafReactiveViewResolver) : WebFluxConfigurer {
    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(thymeleafReactiveViewResolver)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/templates/**")
    }
}