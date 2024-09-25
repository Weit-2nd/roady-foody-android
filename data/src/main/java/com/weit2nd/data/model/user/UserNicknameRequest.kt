package com.weit2nd.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserNicknameRequest(
    @Json(name = "nickname") val nickname: String,
)
