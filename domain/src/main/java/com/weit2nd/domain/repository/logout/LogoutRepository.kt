package com.weit2nd.domain.repository.logout

interface LogoutRepository {
    suspend fun logout()

    suspend fun withdraw()
}
