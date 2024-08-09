package com.weit2nd.domain.model.spot

import java.time.DayOfWeek
import java.time.LocalTime

data class OperationHour(
    val dayOfWeek: DayOfWeek, // todo: FoodSpotOperationDayOfWeek으로 타입 수정하기
    val openingHours: LocalTime,
    val closingHours: LocalTime,
)
