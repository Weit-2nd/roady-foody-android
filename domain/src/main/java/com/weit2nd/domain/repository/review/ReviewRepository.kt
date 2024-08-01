package com.weit2nd.domain.repository.review

import com.weit2nd.domain.model.review.PostReviewVerificationState

interface ReviewRepository {
    suspend fun postReview(
        foodSpotId: Long,
        contents: String,
        rating: Int,
        images: List<String>,
    )

    suspend fun deleteReview(reviewId: Long)

    fun verifyReview(
        contents: String,
        rating: Int,
        images: List<String>,
    ): PostReviewVerificationState
}
