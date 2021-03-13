package ru.itmo.dkudaiberdieva.design.reactive.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.itmo.dkudaiberdieva.design.reactive.entity.User

@Repository
interface UserRepository: ReactiveMongoRepository<User, Long> {
    fun findByUsername(username: String): Mono<User>
}