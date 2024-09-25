package com.weit2nd.data.repository.login

import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.weit2nd.data.BuildConfig
import com.weit2nd.data.source.login.LoginDataSource
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.exception.UnknownException
import com.weit2nd.domain.exception.user.LoginException
import com.weit2nd.domain.model.token.TokenState
import com.weit2nd.domain.repository.login.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.http.HTTP_NOT_FOUND
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    context: Context,
    private val loginDataSource: LoginDataSource,
    private val tokenDataSource: TokenDataSource,
    private val activityProvider: ActivityProvider,
) : LoginRepository {
    init {
        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    override suspend fun loginWithKakao(): Result<Unit> =
        withContext(Dispatchers.IO) {
            val currentActivity =
                activityProvider.currentActivity
                    ?: return@withContext Result.failure(NullPointerException("현재 올라온 activity가 없음"))
            // 카카오 로그인 시도
            val isKakaoTalkLoginAvailable =
                UserApiClient.instance.isKakaoTalkLoginAvailable(currentActivity)
            val kakaoLoginResult =
                if (isKakaoTalkLoginAvailable) {
                    loginDataSource.loginWithKakaoTalk(currentActivity)
                } else {
                    loginDataSource.loginWithKakaoAccount(currentActivity)
                }
            if (kakaoLoginResult.isFailure) {
                return@withContext Result.failure(kakaoLoginResult.exceptionOrNull() ?: Exception())
            }
            login()
        }

    private suspend fun login(): Result<Unit> {
        val serverLoginResult =
            runCatching {
                loginDataSource.loginToServer()
            }
        return if (serverLoginResult.isSuccess) {
            val token = serverLoginResult.getOrThrow()
            tokenDataSource.setAccessToken(token.accessToken)
            tokenDataSource.setRefreshToken(token.refreshToken)
            Result.success(Unit)
        } else {
            val throwable = serverLoginResult.exceptionOrNull() ?: UnknownException()
            Result.failure(handleLoginException(throwable))
        }
    }

    private fun handleLoginException(throwable: Throwable): Throwable {
        return when (throwable) {
            is HttpException -> handleLoginHttpException(throwable)
            else -> throwable
        }
    }

    private fun handleLoginHttpException(throwable: HttpException): Throwable {
        return when (throwable.code()) {
            HTTP_NOT_FOUND -> LoginException.UserNotFoundException()
            HTTP_UNAUTHORIZED -> LoginException.InvalidTokenException()
            else -> throwable
        }
    }

    override suspend fun getTokenState(): TokenState {
        val isRefreshTokenValid = tokenDataSource.checkRefreshTokenValidation()
        if (isRefreshTokenValid.not()) {
            return TokenState.RefreshTokenInvalid
        }

        val isAccessTokenValid = tokenDataSource.checkAccessTokenValidation()
        return if (isAccessTokenValid.not()) {
            val result = runCatching { tokenDataSource.refreshAccessToken() }
            if (result.isSuccess) {
                TokenState.AccessTokenValid
            } else {
                TokenState.FailGettingToken
            }
        } else {
            TokenState.AccessTokenValid
        }
    }
}
