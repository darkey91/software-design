package ru.itmo.sd.fitness.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmo.sd.fitness.repository.EnterEventRepo
import ru.itmo.sd.fitness.repository.LeaveEventRepo
import java.lang.Thread.sleep
import java.time.Duration


@SpringBootTest
class ReportServiceTest {

    @Autowired
    lateinit var enterEventRepo: EnterEventRepo

    @Autowired
    lateinit var leaveEventRepo: LeaveEventRepo

    @Autowired
    lateinit var subscriptionService: SubscriptionService

    @Autowired
    lateinit var reportService: ReportService

    @Autowired
    lateinit var turnstileService: TurnstileService

    @BeforeEach
    fun setUp() {
        enterEventRepo.deleteAll()
        leaveEventRepo.deleteAll()
        reportService.reset()
    }

    @Test
    fun shouldReturnAverageAttendance() {
        val firstSubs = subscriptionService.newSubscription(1L)
        val secondSubs = subscriptionService.newSubscription(1L)

        turnstileService.enter(firstSubs.id!!)
        sleep(Duration.ofSeconds(5).toMillis())
        turnstileService.leave(firstSubs.id!!)

        val avgAttendance = reportService.averageAttendance
        assertEquals(1, avgAttendance.first)
        assertEquals(5.0, avgAttendance.second)

        turnstileService.enter(firstSubs.id!!)
        turnstileService.enter(secondSubs.id!!)
        sleep(Duration.ofSeconds(5).toMillis())
        turnstileService.leave(firstSubs.id!!)
        sleep(Duration.ofSeconds(5).toMillis())
        turnstileService.leave(secondSubs.id!!)

        assertEquals(1, avgAttendance.first)
        assertEquals(5.0, avgAttendance.second)
    }
}