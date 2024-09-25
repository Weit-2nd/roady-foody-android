package com.weit2nd.domain.exception.user

sealed class UserInfoEditException(
    message: String,
) : Throwable(message) {
    class BadRequestException(
        message: String,
    ) : UserInfoEditException(message)

    class DuplicateNicknameException(
        message: String,
    ) : UserInfoEditException(message)
}
