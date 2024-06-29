package com.weit2nd.data.model.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacesDTO(
    @field:Json(name = "items") val items: List<PlaceDTO>,
)
