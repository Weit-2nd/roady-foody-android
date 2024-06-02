package com.weit2nd.domain.repository

interface LoginRepository {
    suspend fun loginWithKakao(): Result<Unit>
}
