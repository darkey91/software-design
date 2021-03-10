package ru.itmo.dkudaiberdieva.soft.design.clock.impl

import ru.itmo.dkudaiberdieva.soft.design.clock.EventsStatistic
import ru.itmo.dkudaiberdieva.soft.design.clock.clock.Clock
import kotlin.math.max

class EventsStatisticImpl(
    private val clock: Clock
) : EventsStatistic {
    private val events = HashMap<String, MutableList<Long>>()

    override fun incEvent(name: String) {
        val now = clock.now().epochSecond
        val event = events.putIfAbsent(name, mutableListOf(now))
        event?.add(now)
    }

    override fun getEventStatisticByName(name: String): Double {
        val eventByName = events[name] ?: return 0.0
        val now = clock.now().epochSecond
        val hourAgo = max(0L, now - SECONDS_IN_HOUR)
        val eventInLastHour = eventByName.filter { it in hourAgo..now }.size
        return eventInLastHour / MINUTES_IN_HOUR.toDouble()
    }

    override fun getAllEventStatistic(): Map<String, Double> =
        events.map { it.key to getEventStatisticByName(it.key) }.toMap()


    override fun printStatistic() {
        events.forEach { (eventName, time) ->
            println("Event: $eventName ; RPM: ${time.size / MINUTES_IN_HOUR.toDouble()}")
        }
    }

    companion object {
        val MINUTES_IN_HOUR = 60
        val SECONDS_IN_HOUR = 3600L
    }
}