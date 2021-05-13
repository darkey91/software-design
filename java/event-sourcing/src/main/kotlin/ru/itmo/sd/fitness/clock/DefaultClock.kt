package ru.itmo.sd.fitness.clock

import java.time.LocalDateTime

class DefaultClock : Clock {
    override fun now(): LocalDateTime {
        return LocalDateTime.now()
    }
}