package com.weit2nd.domain.repository.login

interface LoginRepository {
    suspend fun loginWithKakao(): Result<Unit>
}
