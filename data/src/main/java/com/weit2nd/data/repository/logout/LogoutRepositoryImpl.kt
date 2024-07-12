package com.weit2nd.data.repository.logout

import com.weit2nd.data.source.logout.LogoutDataSource
import com.weit2nd.domain.repository.logout.LogoutRepository
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val logoutDataSource: LogoutDataSource,
) : LogoutRepository {
    override suspend fun logout() {
        logoutDataSource.logout()
        // TODO 인증 토큰 제거
    }

    override suspend fun withdraw() {
        logoutDataSource.withdraw()
        // TODO 인증 토큰 제거
    }
}
