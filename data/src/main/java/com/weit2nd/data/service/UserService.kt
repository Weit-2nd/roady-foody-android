package com.weit2nd.data.service

import com.weit2nd.data.model.user.UserDTO
import retrofit2.http.GET

interface UserService {
    @GET("/api/v1/users/me")
    suspend fun getMyUserInfo(): UserDTO
}
