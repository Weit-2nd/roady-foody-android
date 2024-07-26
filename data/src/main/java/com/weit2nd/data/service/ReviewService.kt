package com.weit2nd.data.service

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReviewService {
    @Multipart
    @POST("/api/v1/review")
    suspend fun postReview(
        @Part reviewRequest: MultipartBody.Part,
        @Part reviewPhotos: List<MultipartBody.Part>?,
    )
}
