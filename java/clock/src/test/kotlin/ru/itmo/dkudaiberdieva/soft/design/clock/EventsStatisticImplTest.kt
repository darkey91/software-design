package ru.itmo.dkudaiberdieva.soft.design.clock

import org.junit.Test
import ru.itmo.dkudaiberdieva.soft.design.clock.clock.impl.SettableClock
import ru.itmo.dkudaiberdieva.soft.design.clock.impl.EventsStatisticImpl
import ru.itmo.dkudaiberdieva.soft.design.clock.impl.EventsStatisticImpl.Companion.MINUTES_IN_HOUR
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EventsStatisticImplTest {

    @Test
    fun `no test in last hour`() {
        val eventStatistic = EventsStatisticImpl(SettableClock(Instant.ofEpochSecond(0L)))
        val eventName = "test"
        assertTrue(eventStatistic.getAllEventStatistic().isEmpty())
        assertTrue(equals(eventStatistic.getEventStatisticByName(eventName), 0.0))
    }

    @Test
    fun `changing time - one incEvent - one event`() {
        val clock = SettableClock (Instant.ofEpochSecond(0L))
        val statsManager = EventsStatisticImpl (clock)
        val eventName = "test"
        statsManager.incEvent(eventName)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(123)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3600)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3601)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun `changing time - few incEvent in hour - few events`() {
        val clock = SettableClock (Instant.ofEpochSecond(0L))
        val statsManager = EventsStatisticImpl (clock)
        val eventName = "test"
        statsManager.incEvent(eventName)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(123)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3600)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        statsManager.incEvent(eventName)
        clock.now = Instant.ofEpochSecond(3600)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 2.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3601)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 1.0 / MINUTES_IN_HOUR))
        assertEquals(1, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun `many increments in one hour`() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = EventsStatisticImpl(clock)
        val eventName = "test"
        val count = 100
        for (i in 1 until count) {
            statsManager.incEvent(eventName)
            assertTrue(equals(statsManager.getEventStatisticByName(eventName), i.toDouble() / MINUTES_IN_HOUR))
            assertEquals(1, statsManager.getAllEventStatistic().size)
        }
    }

    @Test
    fun `no incEvent`() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = EventsStatisticImpl(clock)
        val eventName = "test"

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertEquals(0, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(123)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertEquals(0, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3800)

        assertTrue(equals(statsManager.getEventStatisticByName(eventName), 0.0))
        assertEquals(0, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun `getEventStatistic for non-existing event`() {
        val clock = SettableClock(Instant.ofEpochSecond(0L))
        val statsManager = EventsStatisticImpl(clock)
        val eventName = "test1"
        val anotherEventName = "test2"
        statsManager.incEvent(eventName)

        assertTrue(equals(statsManager.getEventStatisticByName(anotherEventName), 0.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(123)

        assertTrue(equals(statsManager.getEventStatisticByName(anotherEventName), 0.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)

        clock.now = Instant.ofEpochSecond(3800)

        assertTrue(equals(statsManager.getEventStatisticByName(anotherEventName), 0.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)
    }

    @Test
    fun `non-existing event name for getEventStatisticByName`() {
        val statsManager = EventsStatisticImpl(SettableClock(Instant.ofEpochSecond(0L)))
        val eventName = "test1"
        val anotherEventName = "test2"
        statsManager.incEvent(eventName)

        assertTrue(equals(statsManager.getEventStatisticByName(anotherEventName), 0.0))
        assertEquals(1, statsManager.getAllEventStatistic().size)
    }

    private fun equals(lhs: Double, rhs: Double) = kotlin.math.abs(lhs - rhs) <= EPS

    companion object {
        private val EPS = 1e-7
    }
}