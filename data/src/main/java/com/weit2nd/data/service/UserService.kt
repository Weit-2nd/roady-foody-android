package com.weit2nd.data.service

import com.weit2nd.data.model.review.UserReviewsDTO
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.model.user.UserNicknameRequest
import com.weit2nd.data.model.user.UserStatisticsDTO
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/users/me")
    suspend fun getMyUserInfo(): UserDTO

    @GET("/api/v1/users/{userId}/food-spots/histories")
    suspend fun getFoodSpotHistories(
        @Path("userId") userId: Long,
        @Query("size") size: Int,
        @Query("lastId") lastId: Long?,
    ): FoodSpotHistoriesDTO

    @GET("/api/v1/users/{userId}/reviews")
    suspend fun getUserReviews(
        @Path("userId") userId: Long,
        @Query("size") size: Int,
        @Query("lastId") lastId: Long?,
    ): UserReviewsDTO

    @Multipart
    @PATCH("/api/v1/users/profile")
    suspend fun editUserProfile(
        @Part image: MultipartBody.Part?,
    )

    @PATCH("/api/v1/users/nickname")
    suspend fun editUserNickname(
        @Body userNicknameRequest: UserNicknameRequest,
    )

    @GET("/api/v1/users/{userId}/statistics")
    suspend fun getUserStatistics(
        @Path("userId") userId: Long,
    ): UserStatisticsDTO
}
