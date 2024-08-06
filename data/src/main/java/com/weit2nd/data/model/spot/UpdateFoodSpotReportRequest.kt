package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateFoodSpotReportRequest(
    @Json(name = "name") val name: String?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "open") val open: Boolean?,
    @Json(name = "closed") val closed: Boolean?,
    @Json(name = "foodCategories") val foodCategories: List<Long>?,
    @Json(name = "operationHours") val operationHours: List<OperationHourRequest>?,
)
