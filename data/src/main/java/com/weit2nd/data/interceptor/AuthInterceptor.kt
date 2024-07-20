package com.weit2nd.data.interceptor

import com.weit2nd.data.source.token.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authorization =
            runBlocking {
                BEARER_PREFIX + dataSource.getAccessToken()
            }
        val newRequest =
            chain.request().newBuilder().apply {
                addHeader(AUTHORIZATION_HEADER, authorization)
            }
        return chain.proceed(newRequest.build())
    }

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }
}
