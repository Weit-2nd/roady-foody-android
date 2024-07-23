package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodSpotsDTO(
    @field:Json(name = "items") val items: List<FoodSpotDTO>,
)
