package com.weit2nd.data.model.category

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodCategoryDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
)
