package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceWithCoordinateDTO(
    @Json(name = "roadAddressName") val roadAddressName: String?,
    @Json(name = "addressName") val addressName: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
)
