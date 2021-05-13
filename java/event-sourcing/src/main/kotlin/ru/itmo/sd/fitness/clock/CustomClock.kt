package ru.itmo.sd.fitness.clock

import java.time.LocalDateTime

class CustomClock : Clock {
    private var now = LocalDateTime.now()
    override fun now(): LocalDateTime {
        return now
    }

    fun setNow(now: LocalDateTime) {
        this.now = now
    }
}