package com.weit2nd.data.source.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.kakao.sdk.auth.AuthApiClient
import com.weit2nd.data.TokenPreferences
import com.weit2nd.data.service.RefreshTokenService
import com.weit2nd.domain.exception.auth.AuthException
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val context: Context,
    private val refreshTokenService: RefreshTokenService,
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
        context.accessTokenDataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .setToken(token)
                .setCreatedAt(System.currentTimeMillis())
                .build()
        }
    }

    suspend fun getAccessToken(): String? =
        context.accessTokenDataStore.data
            .firstOrNull()
            ?.token

    suspend fun setRefreshToken(token: String) {
        context.refreshTokenDataStore.updateData { preferences ->
            preferences
                .toBuilder()
                .setToken(token)
                .setCreatedAt(System.currentTimeMillis())
                .build()
        }
    }

    suspend fun getRefreshToken(): String? =
        context.refreshTokenDataStore.data
            .firstOrNull()
            ?.token

    suspend fun refreshAccessToken() {
        // TODO 리프레쉬 토큰이 없을 경우 처리 추가
        val token = refreshTokenService.refreshAccessToken(getRefreshToken() ?: "")
        setAccessToken(token.accessToken)
        setRefreshToken(token.refreshToken)
    }

    companion object {
        private const val ACCESS_TOKEN_FILE_NAME = "access_token.pb"
        private const val REFRESH_TOKEN_FILE_NAME = "refresh_token.pb"
    }
}
