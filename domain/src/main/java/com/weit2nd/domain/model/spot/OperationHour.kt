package com.weit2nd.domain.model.spot

import java.time.DayOfWeek
import java.time.LocalTime

data class OperationHour(
    val dayOfWeek: DayOfWeek,
    val openingHours: LocalTime,
    val closingHours: LocalTime,
)
