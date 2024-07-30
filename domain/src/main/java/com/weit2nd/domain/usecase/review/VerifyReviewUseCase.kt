package com.weit2nd.domain.usecase.review

import com.weit2nd.domain.model.review.PostReviewVerificationState
import com.weit2nd.domain.repository.review.ReviewRepository
import javax.inject.Inject

class VerifyReviewUseCase @Inject constructor(
    private val repository: ReviewRepository,
) {
    operator fun invoke(
        contents: String,
        rating: Int,
        images: List<String>,
    ): PostReviewVerificationState {
        return repository.verifyReview(
            contents = contents,
            rating = rating,
            images = images,
        )
    }
}
