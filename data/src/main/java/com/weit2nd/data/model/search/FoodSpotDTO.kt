package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.util.StringToBusinessState
import com.weit2nd.data.util.StringToLocalDateTime
import com.weit2nd.domain.model.search.BusinessState
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class FoodSpotDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "longitude") val longitude: Double,
    @field:Json(name = "latitude") val latitude: Double,
    @StringToBusinessState @field:Json(name = "open") val open: BusinessState,
    @Json(name = "operationHours") val operationHour: OperationHourDTO,
    @Json(name = "foodCategories") val categories: List<String>,
    @Json(name = "imageUrl") val image: String? = null,
    @Json(name = "foodTruck") val isFoodTruck: Boolean,
    @field:Json(name = "averageRating") val averageRating: Float,
    @field:Json(name = "reviewCount") val reviewCount: Int,
    @StringToLocalDateTime @Json(name = "createdDateTime") val createAt: LocalDateTime,
)
