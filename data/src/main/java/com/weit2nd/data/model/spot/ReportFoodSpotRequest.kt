package com.weit2nd.data.model.spot

// TODO 100퍼 바뀜
data class ReportFoodSpotRequest(
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val foodTruck: Boolean,
    val open: Boolean,
    val closed: Boolean,
)
