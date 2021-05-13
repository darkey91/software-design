package ru.itmo.sd.fitness.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.itmo.sd.fitness.event.CreateSubscriptionEvent
import ru.itmo.sd.fitness.event.ExtendSubscriptionEvent
import ru.itmo.sd.fitness.event.LeaveEvent

@Repository
interface ExtendSubscriptionEventRepo: MongoRepository<ExtendSubscriptionEvent, String> {
    fun findBySubscriptionId(subscriptionId: String): List<ExtendSubscriptionEvent>
}