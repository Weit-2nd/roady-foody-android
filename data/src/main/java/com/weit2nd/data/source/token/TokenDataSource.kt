package com.weit2nd.data.source.token

import com.kakao.sdk.auth.AuthApiClient
import com.weit2nd.data.service.RefreshTokenService
import com.weit2nd.domain.exception.auth.AuthException
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val refreshTokenService: RefreshTokenService,
) {
    // TODO 토큰 보관 로적 추가 예정. 이렇게 저장해서 쓰면 안됨
    private var accessToken = ""
    private var refreshToken = ""

    fun getSocialAccessToken(): String =
        AuthApiClient.instance.tokenManagerProvider.manager
            .getToken()
            ?.accessToken
            ?: throw AuthException.TokenNotFoundException()

    fun setAccessToken(token: String) {
        accessToken = token
    }

    fun getAccessToken(): String = accessToken

    fun setRefreshToken(token: String) {
        refreshToken = token
    }

    fun getRefreshToken(): String = refreshToken

    suspend fun refreshAccessToken() {
        val token = refreshTokenService.refreshAccessToken(getRefreshToken())
        setAccessToken(token.accessToken)
        setRefreshToken(token.refreshToken)
    }
}
