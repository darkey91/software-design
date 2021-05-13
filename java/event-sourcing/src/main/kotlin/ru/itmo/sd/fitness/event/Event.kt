package ru.itmo.sd.fitness.event

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.*

abstract class Event(val subscriptionId: String) {
    @Id
    var id = UUID.randomUUID().toString()
    var createdAt = LocalDateTime.now()
}