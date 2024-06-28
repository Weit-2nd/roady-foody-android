package com.weit2nd.domain.exception.user

sealed class SignUpException(
    message: String,
) : Throwable(message) {

    class BadRequestException(
        message: String,
    ) : SignUpException(message)

    class DuplicateUserException(
        message: String,
    ) : SignUpException(message)

    class InvalidTokenException(
        message: String,
    ) : SignUpException(message)
}
