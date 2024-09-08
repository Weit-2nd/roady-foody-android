package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.util.StringToDayOfWeek
import com.weit2nd.data.util.StringToLocalTime
import java.time.DayOfWeek
import java.time.LocalTime

@JsonClass(generateAdapter = true)
data class OperationHourDTO(
    @field:Json(name = "foodSpotsId") val foodSpotsId: Long,
    @StringToDayOfWeek @field:Json(name = "dayOfWeek") val dayOfWeek: DayOfWeek,
    @StringToLocalTime @field:Json(name = "openingHours") val openingHours: LocalTime,
    @StringToLocalTime @field:Json(name = "closingHours") val closingHours: LocalTime,
)
