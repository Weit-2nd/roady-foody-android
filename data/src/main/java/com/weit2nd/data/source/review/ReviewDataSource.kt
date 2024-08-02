package com.weit2nd.data.source.review

import com.weit2nd.data.service.ReviewService
import okhttp3.MultipartBody
import javax.inject.Inject

class ReviewDataSource @Inject constructor(
    private val service: ReviewService,
) {
    suspend fun postReview(
        reviewRequest: MultipartBody.Part,
        reviewPhotos: List<MultipartBody.Part>?,
    ) {
        service.postReview(
            reviewRequest = reviewRequest,
            reviewPhotos = reviewPhotos,
        )
    }

    suspend fun deleteReview(reviewId: Long) {
        service.deleteReview(reviewId)
    }
}
