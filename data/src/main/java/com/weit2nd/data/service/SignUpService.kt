package com.weit2nd.data.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignUpService {

    @Multipart
    @POST("/api/v1/auth")
    suspend fun signUp(
        @Part signUp: MultipartBody.Part,
        @Part image: MultipartBody.Part,
    )
}
