package ru.itmo.sd.fitness.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.sd.fitness.exception.ServiceException
import ru.itmo.sd.fitness.repository.CreateSubscriptionEventRepo
import ru.itmo.sd.fitness.repository.ExtendSubscriptionEventRepo
import java.time.LocalDate

@SpringBootTest
class SubscriptionServiceTest {

    @Autowired
    lateinit var createEventRepo: CreateSubscriptionEventRepo

    @Autowired
    lateinit var extendEventRepo: ExtendSubscriptionEventRepo

    @Autowired
    lateinit var service: SubscriptionService


    @BeforeEach
    fun setUp() {
        createEventRepo.deleteAll()
        extendEventRepo.deleteAll()
    }

    @Test
    fun shouldCreateNewSubscription() {
        val periodInMonth = 3L
        val subscription = service.newSubscription(periodInMonth)

        assertNotNull(subscription)
        assertNotNull(subscription.id)
        assertNotNull(subscription.validTo)
        assertEquals(LocalDate.now().plusMonths(periodInMonth), subscription.validTo)
    }

    @Test
    fun shouldExtendSubscriptionFromNowIfItWasNotExpired() {
        val subscription = service.newSubscription(2L)

        val extendedSubscription = service.extendSubscription(subscription.id!!, 4L)

        assertEquals(LocalDate.now().plusMonths(6L), extendedSubscription.validTo)
    }

    @Test
    fun shouldFindSubscriptionById() {
        val subscription = service.newSubscription(2L)
        service.extendSubscription(subscription.id!!, 4L)
        val extendedSubscription = service.extendSubscription(subscription.id!!, 3L)

        assertEquals(subscription.id, extendedSubscription.id)

        val foundSubscription = service.getSubscriptionById(subscription.id!!)

        assertEquals(subscription.id, foundSubscription.id)
        assertEquals(LocalDate.now().plusMonths(9L), foundSubscription.validTo)
    }

    @Test
    fun shouldThrowExceptionWhenSubscriptionNotFound() {
        assertThrows<ServiceException> { service.getSubscriptionById("1") }
    }

    @Test
    fun shouldThrowExceptionWhenExtendSubscriptionWithoutCreation() {
        assertThrows<ServiceException> { service.extendSubscription("1", 4L) }
    }
}