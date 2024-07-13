package com.weit2nd.data.service

import com.weit2nd.data.model.login.LoginToken
import retrofit2.http.GET
import retrofit2.http.Query

interface RefreshTokenService {
    @GET("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Query("token") token: String,
    ): LoginToken
}
