package com.weit2nd.data.service

import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
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

    @GET("/api/v1/food-spots/histories/{userId}")
    suspend fun getFoodSpotHistories(
        @Path("userId") userId: Long,
        @Query("size") size: Int,
        @Query("lastId") lastId: Long?,
    ): FoodSpotHistoriesDTO

    @DELETE("/api/v1/food-spots/histories/{historyId}")
    suspend fun deleteFoodSpotHistory(
        @Path("historyId") historyId: Long,
    )
}
