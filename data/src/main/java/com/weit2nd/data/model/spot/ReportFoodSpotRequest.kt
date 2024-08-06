package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.domain.model.spot.OperationHour
import java.time.format.DateTimeFormatter

data class ReportFoodSpotRequest(
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val foodTruck: Boolean,
    val open: Boolean,
    val closed: Boolean,
    val foodCategories: List<Long>,
    val operationHours: List<OperationHourRequest>,
)

@JsonClass(generateAdapter = true)
data class OperationHourRequest(
    @Json(name = "dayOfWeek") val dayOfWeek: String,
    @Json(name = "openingHours") val openingHours: String,
    @Json(name = "closingHours") val closingHours: String,
)

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun OperationHour.toRequest() =
    OperationHourRequest(
        dayOfWeek = dayOfWeek.name.substring(0, 3),
        openingHours = openingHours.format(timeFormatter),
        closingHours = closingHours.format(timeFormatter),
    )
