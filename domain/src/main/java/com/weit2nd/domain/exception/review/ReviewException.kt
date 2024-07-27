package com.weit2nd.domain.exception.review

sealed class ReviewException(
    message: String,
) : Throwable(message) {
    class BadRequestException(
        message: String,
    ) : ReviewException(message)

    class FoodSpotNotFoundException(
        message: String,
    ) : ReviewException(message)
}
