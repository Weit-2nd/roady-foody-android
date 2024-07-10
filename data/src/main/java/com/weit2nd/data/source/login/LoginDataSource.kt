package com.weit2nd.data.source.login

import android.app.Activity
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.weit2nd.data.model.login.LoginToken
import com.weit2nd.data.service.LoginService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val loginService: LoginService,
) {
    suspend fun loginWithKakaoTalk(activity: Activity): Result<Unit> =
        callbackFlow {
            UserApiClient.instance.loginWithKakaoTalk(activity) { _, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        trySend(Result.failure(error))
                    }
                    launch {
                        trySend(loginWithKakaoAccount(activity))
                    }
                } else {
                    trySend(Result.success(Unit))
                }
            }
            awaitClose { /* Do Nothing */ }
        }.first()

    suspend fun loginWithKakaoAccount(activity: Activity): Result<Unit> =
        callbackFlow<Result<Unit>> {
            UserApiClient.instance.loginWithKakaoAccount(activity) { _, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                } else {
                    trySend(Result.success(Unit))
                }
            }
            awaitClose { /* Do Nothing */ }
        }.first()

    suspend fun loginToServer(): LoginToken {
        return loginService.signIn()
    }
}
