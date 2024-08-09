package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.model.category.FoodCategoryDTO
import com.weit2nd.data.util.StringToLocalDateTime
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class FoodSpotDetailDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "longitude") val longitude: Double,
    @field:Json(name = "latitude") val latitude: Double,
    @field:Json(name = "movableFoodSpots") val movableFoodSpots: Boolean,
    @field:Json(name = "open") val openState: String,
    @field:Json(name = "storeClosure") val storeClosure: Boolean,
    @field:Json(name = "operationHoursList") val operationHoursList: List<FoodSpotDetailOperationHoursDTO>,
    @field:Json(name = "foodCategoryList") val foodCategoryList: List<FoodCategoryDTO>,
    @field:Json(name = "foodSpotsPhotos") val foodSpotsPhotos: List<FoodSpotPhotoDTO>,
    @field:Json(name = "createdDateTime") @StringToLocalDateTime val createdDateTime: LocalDateTime,
)

@JsonClass(generateAdapter = true)
data class FoodSpotDetailOperationHoursDTO(
    @field:Json(name = "foodSpotsId") val foodSpotsId: Long,
    @field:Json(name = "dayOfWeek") val dayOfWeek: String,
    @field:Json(name = "openingHours") val openingHours: String,
    @field:Json(name = "closingHours") val closingHours: String,
)
