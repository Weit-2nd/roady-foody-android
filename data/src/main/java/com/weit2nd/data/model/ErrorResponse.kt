package com.weit2nd.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "code")
    val code: Long,
    @Json(name = "errorMessage")
    val errorMessage: String
)
