package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TouristSpotDTO(
    @Json(name = "title") val title: String,
    @Json(name = "mainAddr") val mainAddress: String,
    @Json(name = "secondaryAddr") val secondaryAddress: String,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "tel") val tel: String,
    @Json(name = "thumbnailImage") val thumbnailImage: String,
    @Json(name = "tourismType") val tourismType: String,
)
