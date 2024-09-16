package com.weit2nd.data.service

import com.weit2nd.data.model.review.UserReviewsDTO
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.user.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/api/v1/users/me")
    suspend fun getMyUserInfo(): UserDTO

    @GET("/api/v1/users/{userId}/food_spots/histories")
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
}
