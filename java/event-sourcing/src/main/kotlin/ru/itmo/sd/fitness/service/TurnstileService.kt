package ru.itmo.sd.fitness.service

import org.springframework.stereotype.Service
import ru.itmo.sd.fitness.event.EnterEvent
import ru.itmo.sd.fitness.event.Event
import ru.itmo.sd.fitness.event.LeaveEvent
import ru.itmo.sd.fitness.exception.ServiceException
import ru.itmo.sd.fitness.projector.SubscriptionProjector
import ru.itmo.sd.fitness.repository.EnterEventRepo
import ru.itmo.sd.fitness.repository.LeaveEventRepo
import java.time.LocalDateTime

@Service
class TurnstileService(
    private val subscriptionService: SubscriptionService,
    private val enterEventRepo: EnterEventRepo,
    private val leaveEventRepo: LeaveEventRepo,
    private val subscriptionProjector: SubscriptionProjector,
    private val reportService: ReportService
) {

    fun enter(sId: String) {
        val now = LocalDateTime.now()
        val subscription = subscriptionService.getSubscriptionById(sId)

        if (subscription.isExpired(now.toLocalDate())) {
            throw ServiceException("Subscription is expired")
        }
        subscription.validateEntrance()

        val enterEvent = EnterEvent(sId, now)
        enterEventRepo.save(enterEvent)
        reportService.updateAttendance(now)
    }

    fun leave(sId: String) {
        val now = LocalDateTime.now()
        val subscription = subscriptionService.getSubscriptionById(sId)

        if (subscription.isExpired(now.toLocalDate())) {
            throw ServiceException("Subscription is expired")
        }
        subscription.validateLeaving()

        val leaveEvent = LeaveEvent(sId, now)
        leaveEventRepo.save(leaveEvent)
        reportService.updateAverageAttendance(now, subscription.id!!)
    }

}
