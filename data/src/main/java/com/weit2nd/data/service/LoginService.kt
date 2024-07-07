package com.weit2nd.data.service

import com.weit2nd.data.model.login.LoginToken
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LoginService {

    @Multipart
    @POST("/api/v1/auth")
    suspend fun signUp(
        @Part signUp: MultipartBody.Part,
        @Part image: MultipartBody.Part,
    )

    @GET("/api/v1/auth")
    suspend fun signIn(): LoginToken
}
