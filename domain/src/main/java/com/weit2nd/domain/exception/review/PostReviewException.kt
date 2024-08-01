package com.weit2nd.domain.exception.review

sealed class PostReviewException(
    message: String,
) : Throwable(message) {
    class BadRequestException(
        message: String,
    ) : PostReviewException(message)

    class FoodSpotNotFoundException(
        message: String,
    ) : PostReviewException(message)
}
