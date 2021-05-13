package ru.itmo.sd.fitness.event

import org.hibernate.validator.constraints.Range
import org.springframework.data.mongodb.core.mapping.Document

@Document
class CreateSubscriptionEvent(
    subscriptionId: String,
    @Range(min = 1L, max = 12L)
    val periodInMonths: Long
) : Event(subscriptionId)