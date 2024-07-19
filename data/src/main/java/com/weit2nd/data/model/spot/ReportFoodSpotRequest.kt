package com.weit2nd.data.model.spot

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

data class OperationHourRequest(
    val dayOfWeek: String,
    val openingHours: String,
    val closingHours: String,
)

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun OperationHour.toRequest() =
    OperationHourRequest(
        dayOfWeek = dayOfWeek.name.substring(0, 3),
        openingHours = openingHours.format(timeFormatter),
        closingHours = closingHours.format(timeFormatter),
    )
