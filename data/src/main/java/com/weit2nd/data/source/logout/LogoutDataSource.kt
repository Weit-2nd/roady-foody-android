package com.weit2nd.data.source.logout

import com.kakao.sdk.user.UserApiClient
import com.weit2nd.data.service.LogoutService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
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

    // 성공이냐 실패냐는 크게 중요하지 않지만 결과가 나올때 까지는 기다려야 하기 때문에
    // logout 콜백에서 값을 넘길 때 까지 기다림
    suspend fun logoutToKakao() =
        callbackFlow {
            UserApiClient.instance.logout {
                trySend(Unit)
            }
            awaitClose { }
        }.first()
}
