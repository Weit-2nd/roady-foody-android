package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewInfoDTO(
    @Json(name = "avgRating") val average: Float,
    @field:Json(name = "reviewCount") val reviewCount: Int,
)
