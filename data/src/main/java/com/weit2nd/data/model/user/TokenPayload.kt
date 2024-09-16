package com.weit2nd.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenPayload(
    @field:Json(name = "userId") val userId: Long,
    @field:Json(name = "exp") val exp: Long,
)
