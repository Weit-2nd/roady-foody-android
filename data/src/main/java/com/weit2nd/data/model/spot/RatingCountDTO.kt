package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingCountDTO(
    @field:Json(name = "rating") val rating: Int,
    @field:Json(name = "count") val count: Int,
)
