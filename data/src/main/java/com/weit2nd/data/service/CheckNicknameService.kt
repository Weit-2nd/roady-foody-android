package com.weit2nd.data.service

import com.weit2nd.data.model.user.CheckNicknameDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface CheckNicknameService {
    @GET("/api/v1/auth/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): CheckNicknameDTO
}
