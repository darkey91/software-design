package ru.itmo.dkudaiberdieva.design.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveProjectApplication

fun main(args: Array<String>) {
	runApplication<ReactiveProjectApplication>(*args)
}
