package ru.itmo.sd.fitness.dto

import ru.itmo.sd.fitness.exception.ServiceException
import java.time.LocalDate

data class Subscription(
    var id: String? = null,
    var validFrom: LocalDate? = null,
    var validTo: LocalDate? = null,
    var inUse: Boolean = false
) {
    fun isExpired(day: LocalDate) = validTo == null || validTo!!.isBefore(day)

    fun validate() {
        if (validTo == null || id == null) throw ServiceException("There is no subscription!")
    }

    fun validateEntrance() {
        if (inUse) throw ServiceException("Can not enter. Subscription is already in use!")
    }

    fun validateLeaving() {
        if (!inUse) throw ServiceException("Can not leave because nobody entered :)")
    }

}
