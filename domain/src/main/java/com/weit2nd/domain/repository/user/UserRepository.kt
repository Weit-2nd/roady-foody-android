package com.weit2nd.domain.repository.user

import com.weit2nd.domain.model.UserInfo

interface UserRepository {
    suspend fun getUserInfo(): UserInfo
}
