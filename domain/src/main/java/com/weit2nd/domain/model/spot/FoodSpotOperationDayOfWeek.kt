package com.weit2nd.domain.model.spot

import java.time.DayOfWeek

enum class FoodSpotOperationDayOfWeek(
    val dayOfWeek: DayOfWeek,
) {
    MON(DayOfWeek.MONDAY),
    TUE(DayOfWeek.TUESDAY),
    WED(DayOfWeek.WEDNESDAY),
    THU(DayOfWeek.THURSDAY),
    FRI(DayOfWeek.FRIDAY),
    SAT(DayOfWeek.SATURDAY),
    SUN(DayOfWeek.SUNDAY),
    ;

    companion object {
        fun findDayOfWeek(dayOfWeek: String): FoodSpotOperationDayOfWeek {
            return entries.find { it.name == dayOfWeek } ?: throw NoSuchElementException()
        }
    }
}
