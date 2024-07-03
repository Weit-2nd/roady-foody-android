package com.weit2nd.data.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SpotService {
    @Multipart
    @POST("/api/v1/food-spots")
    suspend fun reportFoodSpot(
        @Part reportRequest: MultipartBody.Part,
        @Part reportPhotos: List<MultipartBody.Part>?,
    )
}
