package com.weit2nd.data.service

import retrofit2.http.POST

interface LogoutService {
    @POST("/api/v1/auth/sign-out")
    suspend fun logout()

    @POST("/api/v1/auth/withdraw")
    suspend fun withdraw()
}
