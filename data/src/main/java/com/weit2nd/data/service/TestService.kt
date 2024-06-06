package com.weit2nd.data.service

import retrofit2.http.GET
import retrofit2.http.Query

interface TestService {
    @GET("/api/v1/test/success")
    suspend fun getSuccess(
        @Query("name") name: String,
    ): String
}
