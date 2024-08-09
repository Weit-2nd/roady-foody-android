package com.weit2nd.data.service

import com.weit2nd.data.model.spot.FoodSpotDetailDTO
import com.weit2nd.data.model.spot.FoodSpotReviewsDTO
import com.weit2nd.data.model.spot.UpdateFoodSpotReportRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotService {
    @Multipart
    @POST("/api/v1/food-spots")
    suspend fun reportFoodSpot(
        @Part reportRequest: MultipartBody.Part,
        @Part reportPhotos: List<MultipartBody.Part>?,
    )

    @DELETE("/api/v1/food-spots/histories/{historyId}")
    suspend fun deleteFoodSpotHistory(
        @Path("historyId") historyId: Long,
    )

    @PATCH("/api/v1/food-spots/{foodSpotsId}")
    suspend fun updateFoodSpotReport(
        @Path("foodSpotsId") foodSpotsId: Long,
        @Body request: UpdateFoodSpotReportRequest,
    )

    @GET("/api/v1/food-spots/{foodSpotsId}/reviews")
    suspend fun getFoodSpotReviews(
        @Path("foodSpotsId") foodSpotsId: Long,
        @Query("size") size: Int,
        @Query("lastId") lastId: Long?,
        @Query("sortType") sortType: String,
    ): FoodSpotReviewsDTO

    @GET("/api/v1/food-spots/{foodSpotsId}")
    suspend fun getFoodSpotDetail(
        @Path("foodSpotsId") foodSpotsId: Long,
    ): FoodSpotDetailDTO
}
