package com.weit2nd.data.interceptor

import com.weit2nd.data.source.token.TokenDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // TODO 만료된 액세스 토큰 갱신 로직 추가
        val authorization = BEARER_PREFIX + dataSource.getAccessToken()
        val newRequest =
            chain.request().newBuilder().apply {
                addHeader(AUTHORIZATION_HEADER, authorization)
            }
        return chain.proceed(newRequest.build())
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }
}
