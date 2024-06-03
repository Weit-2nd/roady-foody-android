package com.weit2nd.data.repository.login

import com.kakao.sdk.user.UserApiClient
import com.weit2nd.data.source.login.LoginDataSource
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.repository.login.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource,
    private val activityProvider: ActivityProvider,
) : LoginRepository {
    override suspend fun loginWithKakao(): Result<Unit> = withContext(Dispatchers.IO) {
        val currentActivity = activityProvider.currentActivity
            ?: return@withContext Result.failure(NullPointerException("현재 올라온 activity가 없음"))
        // 카카오 로그인 시도
        val isKakaoTalkLoginAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(currentActivity)
        val kakaoLoginResult = if (isKakaoTalkLoginAvailable) {
            loginDataSource.loginWithKakaoTalk(currentActivity)
        } else {
            loginDataSource.loginWithKakaoAccount(currentActivity)
        }
        if (kakaoLoginResult.isFailure) {
            return@withContext Result.failure(kakaoLoginResult.exceptionOrNull() ?: Exception())
        }
        // TODO: 6/2/24 (minseonglove) 카카오 로그인을 성공했으면 서버 로그인 시도
        loginDataSource.loginToServer()
        return@withContext Result.success(Unit)
    }
}
