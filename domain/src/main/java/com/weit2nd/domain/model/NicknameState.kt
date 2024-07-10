package com.weit2nd.domain.model

enum class NicknameState {
    EMPTY,
    INVALID_LENGTH,
    INVALID_CHARACTERS,
    INVALID_CONTAIN_SPACE,
    VALID,
    DUPLICATE,
    CAN_SIGN_UP,
}
