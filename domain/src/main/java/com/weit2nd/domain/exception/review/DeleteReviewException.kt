package com.weit2nd.domain.exception.review

sealed class DeleteReviewException(
    message: String,
) : Throwable(message) {
    class NotReviewOwnerException(
        message: String,
    ) : DeleteReviewException(message)

    class ReviewNotFoundException(
        message: String,
    ) : DeleteReviewException(message)
}
