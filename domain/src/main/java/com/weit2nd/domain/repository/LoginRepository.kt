package com.weit2nd.domain.repository

import com.weit2nd.domain.model.User

interface LoginRepository {
    suspend fun login(): User
}
