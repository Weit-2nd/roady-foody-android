package com.weit2nd.domain.model.review

sealed class PostReviewVerificationState {
    data object InvalidImage : PostReviewVerificationState()

    data object EmptyContents : PostReviewVerificationState()

    data class TooManyContents(
        val maxLength: Int,
    ) : PostReviewVerificationState()

    data object InvalidRating : PostReviewVerificationState()

    data object Valid : PostReviewVerificationState()
}
