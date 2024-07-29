package com.weit2nd.domain.model.token

sealed class TokenState {
    data object AccessTokenValid : TokenState()

    data object RefreshTokenInvalid : TokenState()

    data object FailGettingToken : TokenState()
}
