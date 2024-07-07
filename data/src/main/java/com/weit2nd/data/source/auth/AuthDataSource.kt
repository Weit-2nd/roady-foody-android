package com.weit2nd.data.source.auth

import com.kakao.sdk.auth.AuthApiClient
import com.weit2nd.domain.exception.auth.AuthException

class AuthDataSource {

    // TODO 토큰 보관 로적 추가 예정. 이렇게 저장해서 쓰면 안됨
    private var accessToken = ""
    private var refreshToken = ""

    fun getSocialAccessToken(): String =
        AuthApiClient.instance.tokenManagerProvider.manager.getToken()?.accessToken
            ?: throw AuthException.TokenNotFoundException()

    fun setAccessToken(token: String) {
        accessToken = token
    }

    fun getAccessToken(): String = accessToken

    fun setRefreshToken(token: String) {
        refreshToken = token
    }

    fun getRefreshToken(): String = refreshToken
}
