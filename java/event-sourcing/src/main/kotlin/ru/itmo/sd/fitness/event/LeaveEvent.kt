package ru.itmo.sd.fitness.event

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class LeaveEvent(subscriptionId: String, val leavingTime: LocalDateTime): Event(subscriptionId)