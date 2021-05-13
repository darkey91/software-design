package ru.itmo.sd.fitness.projector

import org.springframework.stereotype.Service
import ru.itmo.sd.fitness.dto.Subscription
import ru.itmo.sd.fitness.event.*

@Service
class SubscriptionProjector {
    fun apply(subscription: Subscription, events: List<Event>) {
        events.sortedBy { it.createdAt }.forEach { event ->
            when (event) {
                is CreateSubscriptionEvent -> {
                    subscription.id = event.subscriptionId
                    subscription.validTo = event.createdAt.plusMonths(event.periodInMonths).toLocalDate()
                }
                is ExtendSubscriptionEvent -> {
                    subscription.validate()
                    val startDate = if (subscription.isExpired(event.createdAt.toLocalDate())) event.createdAt.toLocalDate() else subscription.validTo!!
                    subscription.validTo = startDate.plusMonths(event.periodInMonths)
                }
                is EnterEvent -> {
                    subscription.validateEntrance()
                    subscription.inUse = true
                }
                is LeaveEvent -> {
                    subscription.validateLeaving()
                    subscription.inUse = false
                }
                else -> throw IllegalStateException("Unexpected event $event")
            }
        }
    }
}