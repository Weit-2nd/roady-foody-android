package com.weit2nd.domain.repository.review

import com.weit2nd.domain.model.review.PostReviewState

interface ReviewRepository {
    suspend fun postReview(
        foodSpotId: Long,
        contents: String,
        rating: Int,
        images: List<String>,
    )

    suspend fun verifyReview(
        contents: String,
        rating: Int,
        images: List<String>,
    ): PostReviewState
}
