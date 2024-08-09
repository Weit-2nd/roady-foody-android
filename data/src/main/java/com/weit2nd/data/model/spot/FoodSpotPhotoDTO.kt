package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodSpotPhotoDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "url") val image: String,
)
