package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.util.StringToLocalDateTime
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class FoodSpotDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "longitude") val longitude: Double,
    @field:Json(name = "latitude") val latitude: Double,
    @field:Json(name = "open") val open: String,
    @Json(name = "foodCategories") val categories: List<String>,
    @StringToLocalDateTime @Json(name = "createdDateTime") val createAt: LocalDateTime,
)
