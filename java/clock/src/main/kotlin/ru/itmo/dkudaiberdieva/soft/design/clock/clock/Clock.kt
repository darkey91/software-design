package ru.itmo.dkudaiberdieva.soft.design.clock.clock

import java.time.Instant

interface Clock {
    fun now(): Instant
}