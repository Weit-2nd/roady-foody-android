package com.weit2nd.data.model.spot

import com.weit2nd.domain.model.spot.OperationHour

data class ReportFoodSpotRequest(
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val foodTruck: Boolean,
    val open: Boolean,
    val closed: Boolean,
    val foodCategories: List<Long>,
    val operationHours: List<OperationHour>,
)
