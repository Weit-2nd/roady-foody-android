package com.weit2nd.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @field:Json(name = "nickname") val nickname: String,
    @field:Json(name = "profileImageUrl") val profileImageUrl: String?,
    @field:Json(name = "coin") val coin: Int,
)
