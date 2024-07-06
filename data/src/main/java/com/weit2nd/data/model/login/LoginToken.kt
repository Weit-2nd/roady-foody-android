package com.weit2nd.data.model.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @param accessToken api 인증에 사용하는 토큰
 * @param refreshToken accessToken 만료시 재발급을 위해 사용하는 토큰
 */
@JsonClass(generateAdapter = true)
data class LoginToken(
    @Json(name = "accessToken") val accessToken: String,
    @Json(name = "refreshToken") val refreshToken: String,
)
