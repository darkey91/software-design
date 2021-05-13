package ru.itmo.sd.fitness.service

import org.springframework.stereotype.Service
import ru.itmo.sd.fitness.event.EnterEvent
import ru.itmo.sd.fitness.event.Event
import ru.itmo.sd.fitness.event.LeaveEvent
import ru.itmo.sd.fitness.repository.EnterEventRepo
import ru.itmo.sd.fitness.repository.LeaveEventRepo
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

typealias DailyAttendance = MutableMap<String, Long>

@Service
class ReportService(
    private val enterEventRepo: EnterEventRepo,
    private val leaveEventRepo: LeaveEventRepo
) {
    final var dailyAttendance: DailyAttendance = HashMap()
        private set
        public get() = field

    final var averageAttendance: Pair<Long, Double> = 0L to 0.0
        private set
        public get() = field

    init {
        dailyAttendance = initDailyAttendance()
        averageAttendance = initAverageAttendance()
    }

    fun updateAttendance(day: LocalDateTime) {
        val dateKey = day.toLocalDate().format(DateTimeFormatter.ISO_DATE)
        dailyAttendance.compute(dateKey) { _, v -> if (v == null) 1L else v + 1L }
    }

    fun updateAverageAttendance(dateTime: LocalDateTime, sId: String) {
        val enterEvents = enterEventRepo.findBySubscriptionId(sId).sortedBy { it.createdAt }
        val lastEnterEventTime = enterEvents.last().enteredTime
        val duration = Duration.between(lastEnterEventTime, dateTime).seconds
        val newAttendances = averageAttendance.first + 1
        val newAverageDuration = ((newAttendances - 1) * averageAttendance.second + duration) / newAttendances
        averageAttendance = newAttendances to newAverageDuration
    }

    private fun initDailyAttendance(): DailyAttendance {
        val result = HashMap<String, Long>()
        enterEventRepo.findAll().forEach { event ->
            val day = event.createdAt
            updateAttendance(day)
        }
        return result
    }

    private fun initAverageAttendance(): Pair<Long, Double> {
        val visitEvents = ArrayList<Event>()
        visitEvents.addAll(enterEventRepo.findAll())
        visitEvents.addAll(leaveEventRepo.findAll())

        val eventsBySubscriptionId = visitEvents.groupBy { it.subscriptionId }

        var attendances = 0L
        val durations =  ArrayList<Long>()
        eventsBySubscriptionId.entries.forEach {
            val eventTimes = ArrayList<LocalDateTime>()
            it.value.sortedBy { e -> e.createdAt }.forEach { event ->
                when(event) {
                    is EnterEvent -> {
                        attendances++
                        eventTimes.add(event.enteredTime)
                    }
                    is LeaveEvent -> {
                        val entranceTime = eventTimes.removeLast()
                        val leavingTime = event.leavingTime
                        durations.add(Duration.between(entranceTime, leavingTime).seconds)
                    }
                }
            }
        }
        val averageDuration = if (durations.isEmpty()) 0.0 else  durations.map { it.toDouble() }.average()
        return  attendances to averageDuration
    }

    fun reset() {
        dailyAttendance = initDailyAttendance()
        averageAttendance = initAverageAttendance()
    }
}