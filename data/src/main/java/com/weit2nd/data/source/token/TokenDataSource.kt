package com.weit2nd.data.source.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.kakao.sdk.auth.AuthApiClient
import com.weit2nd.data.TokenPreferences
import com.weit2nd.data.service.RefreshTokenService
import com.weit2nd.data.util.SecurityProvider
import com.weit2nd.domain.exception.auth.AuthException
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val context: Context,
    private val refreshTokenService: RefreshTokenService,
    private val securityProvider: SecurityProvider,
) {
    private val Context.accessTokenDataStore: DataStore<TokenPreferences> by dataStore(
        fileName = ACCESS_TOKEN_FILE_NAME,
        serializer = TokenSerializer,
    )

    private val Context.refreshTokenDataStore: DataStore<TokenPreferences> by dataStore(
        fileName = REFRESH_TOKEN_FILE_NAME,
        serializer = TokenSerializer,
    )

    fun getSocialAccessToken(): String =
        AuthApiClient.instance.tokenManagerProvider.manager
            .getToken()
            ?.accessToken
            ?: throw AuthException.TokenNotFoundException()

    suspend fun setAccessToken(token: String) {
        context.accessTokenDataStore.setToken(token)
    }

    suspend fun getAccessToken(): String? = context.accessTokenDataStore.getToken()

    suspend fun setRefreshToken(token: String) {
        context.refreshTokenDataStore.setToken(token)
    }

    suspend fun getRefreshToken(): String? = context.refreshTokenDataStore.getToken()

    suspend fun refreshAccessToken() {
        // TODO 리프레쉬 토큰이 없을 경우 처리 추가
        val token = refreshTokenService.refreshAccessToken(getRefreshToken() ?: "")
        setAccessToken(token.accessToken)
        setRefreshToken(token.refreshToken)
    }

    private suspend fun DataStore<TokenPreferences>.setToken(token: String) {
        val encryptedToken = securityProvider.encrypt(token)
        updateData { preferences ->
            preferences
                .toBuilder()
                .setToken(encryptedToken)
                .setCreatedAt(System.currentTimeMillis())
                .build()
        }
    }

    private suspend fun DataStore<TokenPreferences>.getToken(): String? {
        val token =
            data
                .firstOrNull()
                ?.token
        return token?.let {
            securityProvider.decrypt(it)
        }
    }

    companion object {
        private const val ACCESS_TOKEN_FILE_NAME = "access_token.pb"
        private const val REFRESH_TOKEN_FILE_NAME = "refresh_token.pb"
    }
}
