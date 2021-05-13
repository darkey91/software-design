package ru.itmo.sd.fitness.service

import org.springframework.stereotype.Service
import ru.itmo.sd.fitness.dto.Subscription
import ru.itmo.sd.fitness.event.CreateSubscriptionEvent
import ru.itmo.sd.fitness.event.Event
import ru.itmo.sd.fitness.event.ExtendSubscriptionEvent
import ru.itmo.sd.fitness.projector.SubscriptionProjector
import ru.itmo.sd.fitness.repository.CreateSubscriptionEventRepo
import ru.itmo.sd.fitness.repository.EnterEventRepo
import ru.itmo.sd.fitness.repository.ExtendSubscriptionEventRepo
import ru.itmo.sd.fitness.repository.LeaveEventRepo
import java.time.LocalDate
import java.util.*

@Service
class SubscriptionService(
    private val enterEventRepo: EnterEventRepo,
    private val leaveEventRepo: LeaveEventRepo,
    private val sCreateEventRepo: CreateSubscriptionEventRepo,
    private val sExtendEventRepo: ExtendSubscriptionEventRepo,
    private val sProjector: SubscriptionProjector
) {

    fun newSubscription(periodInMonths: Long): Subscription {
        val subscriptionId = UUID.randomUUID().toString()
        val validFrom = LocalDate.now()
        val validTo = validFrom.plusMonths(periodInMonths)
        val subscription = Subscription(id = subscriptionId, validFrom = validFrom, validTo = validTo)
        val event = CreateSubscriptionEvent(subscriptionId, periodInMonths)
        sCreateEventRepo.save(event)
        return subscription
    }

    fun extendSubscription(id: String, periodInMonths: Long): Subscription {
        val subscription = Subscription()
        val createEvents = sCreateEventRepo.findBySubscriptionId(id)
        sProjector.apply(subscription, createEvents)
        subscription.validate()
        assert(id == subscription.id)
        val event = ExtendSubscriptionEvent(id, periodInMonths)
        sExtendEventRepo.save(event)
        return getSubscriptionById(id)
    }

    fun getSubscriptionById(id: String): Subscription {
        val subscription = Subscription()
        val events = ArrayList<Event>()
        events.addAll(sCreateEventRepo.findBySubscriptionId(id))
        events.addAll(sExtendEventRepo.findBySubscriptionId(id))
        events.addAll(enterEventRepo.findBySubscriptionId(id))
        events.addAll(leaveEventRepo.findBySubscriptionId(id))
        sProjector.apply(subscription, events)
        subscription.validate()
        assert(id == subscription.id)
        return subscription
    }
}