package com.weit2nd.data.source.logout

import com.weit2nd.data.service.LogoutService
import javax.inject.Inject

class LogoutDataSource @Inject constructor(
    private val service: LogoutService,
) {
    suspend fun logout() {
        service.logout()
    }

    suspend fun withdraw() {
        service.withdraw()
    }
}
