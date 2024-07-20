package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodSpotHistoriesDTO(
    @field:Json(name = "contents") val contents: List<FoodSpotHistoryContentDTO>,
    @field:Json(name = "hasNext") val hasNext: Boolean,
)

@JsonClass(generateAdapter = true)
data class FoodSpotHistoryContentDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "userId") val userId: Long,
    @field:Json(name = "foodSpotsId") val foodSpotsId: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "longitude") val longitude: Double,
    @field:Json(name = "latitude") val latitude: Double,
    @field:Json(name = "createdDateTime") val createdDateTime: String,
    @field:Json(name = "reportPhotos") val reportPhotos: List<FoodSpotReportPhotoDTO>,
    @field:Json(name = "categories") val categories: List<FoodSpotCategoryDTO>,
)

@JsonClass(generateAdapter = true)
data class FoodSpotReportPhotoDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "url") val url: String,
)

@JsonClass(generateAdapter = true)
data class FoodSpotCategoryDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
)
