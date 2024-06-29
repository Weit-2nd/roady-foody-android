package com.weit2nd.data.model.terms

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TermDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "isRequired") val isRequired: Boolean,
)
