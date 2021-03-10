package ru.itmo.dkudaiberdieva.soft.design.clock.clock.impl

import ru.itmo.dkudaiberdieva.soft.design.clock.clock.Clock
import java.time.Instant

class SettableClock(var now: Instant): Clock {
    override fun now(): Instant = now
}