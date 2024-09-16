package com.weit2nd.domain.exception.user

sealed class UserReviewException : Exception() {
    class NoMoreReviewException : UserReviewException()
}
