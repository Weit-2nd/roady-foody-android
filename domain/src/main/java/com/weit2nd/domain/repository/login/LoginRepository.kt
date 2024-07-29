package com.weit2nd.domain.repository.login

import com.weit2nd.domain.exception.token.TokenState

interface LoginRepository {
    suspend fun loginWithKakao(): Result<Unit>

    suspend fun getTokenState(): TokenState
}
