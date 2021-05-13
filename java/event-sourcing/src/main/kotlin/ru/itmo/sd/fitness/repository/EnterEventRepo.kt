package ru.itmo.sd.fitness.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.itmo.sd.fitness.event.CreateSubscriptionEvent
import ru.itmo.sd.fitness.event.EnterEvent

@Repository
interface EnterEventRepo: MongoRepository<EnterEvent, String> {
    fun findBySubscriptionId(subscriptionId: String): List<EnterEvent>
}