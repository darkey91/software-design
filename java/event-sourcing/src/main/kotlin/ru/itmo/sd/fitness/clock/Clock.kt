package ru.itmo.sd.fitness.clock

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}