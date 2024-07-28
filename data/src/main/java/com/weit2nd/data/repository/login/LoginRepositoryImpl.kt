package com.weit2nd.data.repository.login

import com.kakao.sdk.user.UserApiClient
import com.weit2nd.data.source.login.LoginDataSource
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.source.token.TokenInfo
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.exception.UnknownException
import com.weit2nd.domain.exception.token.TokenState
import com.weit2nd.domain.exception.user.LoginException
import com.weit2nd.domain.repository.login.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.http.HTTP_NOT_FOUND
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource,
    private val tokenDataSource: TokenDataSource,
    private val activityProvider: ActivityProvider,
) : LoginRepository {
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

    override suspend fun loginToServer(): Result<Unit> {
        val tokenState = determineTokenState()
        return when (tokenState) {
            TokenState.AccessTokenValid -> login()
            TokenState.RefreshTokenInvalid -> Result.failure(LoginException.InvalidTokenException())
            TokenState.FailGettingToken -> Result.failure(UnknownException())
        }
    }

    private suspend fun determineTokenState(): TokenState {
        val isRefreshTokenValid = checkTokenValidation(tokenDataSource.getRefreshToken(), false)
        if (isRefreshTokenValid.not()) {
            return TokenState.RefreshTokenInvalid
        }

        val isAccessTokenValid = checkTokenValidation(tokenDataSource.getAccessToken(), true)
        if (isAccessTokenValid.not()) {
            val result = runCatching { tokenDataSource.refreshAccessToken() }
            return if (result.isSuccess) {
                TokenState.AccessTokenValid
            } else {
                TokenState.FailGettingToken
            }
        }
        return TokenState.AccessTokenValid
    }

    private fun checkTokenValidation(
        tokenInfo: TokenInfo?,
        isAccessToken: Boolean,
    ): Boolean {
        // TODO token이 null일 경우(없을 경우)를 TokenDataSource.getToken()에 추가
        if (tokenInfo == null) {
            throw UnknownException()
        }
        return if (isAccessToken) {
            tokenInfo.createdTime
                .plusMinutes(ACCESS_TOKEN_EXPIRATION_MINUTE)
                .isAfter(LocalDateTime.now())
        } else {
            tokenInfo.createdTime
                .plusDays(REFRESH_TOKEN_EXPIRATION_DAY)
                .isAfter(LocalDateTime.now())
        }
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

    companion object {
        private const val ACCESS_TOKEN_EXPIRATION_MINUTE = 25.toLong()
        private const val REFRESH_TOKEN_EXPIRATION_DAY = 13.toLong()
    }
}
