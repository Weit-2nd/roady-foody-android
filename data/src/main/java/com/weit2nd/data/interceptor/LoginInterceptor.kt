package com.weit2nd.data.interceptor

import com.weit2nd.data.source.auth.AuthDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LoginInterceptor @Inject constructor(
    private val dataSource: AuthDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authorization = BEARER_PREFIX + dataSource.getSocialAccessToken()
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
