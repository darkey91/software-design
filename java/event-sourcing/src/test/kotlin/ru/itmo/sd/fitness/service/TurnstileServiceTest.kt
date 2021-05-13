package ru.itmo.sd.fitness.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.sd.fitness.exception.ServiceException
import ru.itmo.sd.fitness.repository.EnterEventRepo
import ru.itmo.sd.fitness.repository.LeaveEventRepo

@SpringBootTest
class TurnstileServiceTest {

    @Autowired
    lateinit var enterEventRepo: EnterEventRepo

    @Autowired
    lateinit var leaveEventRepo: LeaveEventRepo

    @Autowired
    lateinit var subscriptionService: SubscriptionService

    @Autowired
    lateinit var turnstileService: TurnstileService

    @AfterEach
    fun setUp() {
        enterEventRepo.deleteAll()
        leaveEventRepo.deleteAll()
    }

    @Test
    fun shouldThrowExceptionWhenEnteringWithoutSubscription() {
        assertThrows<ServiceException> { turnstileService.enter("1") }
    }

    @Test
    fun shouldEnter() {
        val periodInMonth = 3L
        val subscription = subscriptionService.newSubscription(periodInMonth)

        turnstileService.enter(subscription.id!!)

        val foundSubscription = subscriptionService.getSubscriptionById(subscription.id!!)
        assertTrue(foundSubscription.inUse)
    }

    @Test
    fun shouldLeave() {
        val periodInMonth = 3L
        val subscription = subscriptionService.newSubscription(periodInMonth)

        turnstileService.enter(subscription.id!!)

        var foundSubscription = subscriptionService.getSubscriptionById(subscription.id!!)
        assertTrue(foundSubscription.inUse)

        turnstileService.leave(subscription.id!!)

        foundSubscription = subscriptionService.getSubscriptionById(subscription.id!!)
        assertFalse(foundSubscription.inUse)
    }


    @Test
    fun shouldThrowExceptionWhenLeavingAndSubscriptionInUse() {
        val subscription = subscriptionService.newSubscription(1L)
        assertThrows<ServiceException> { turnstileService.leave(subscription.id!!) }
    }

}