package com.weit2nd.data.source.auth

import com.kakao.sdk.auth.AuthApiClient
import com.weit2nd.domain.exception.auth.AuthException

class AuthDataSource {
    fun getSocialAccessToken(): String =
        AuthApiClient.instance.tokenManagerProvider.manager.getToken()?.accessToken
            ?: throw AuthException.TokenNotFoundException()
}
