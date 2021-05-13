package ru.itmo.sd.fitness.event

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class EnterEvent(subscriptionId: String, val enteredTime: LocalDateTime): Event(subscriptionId)