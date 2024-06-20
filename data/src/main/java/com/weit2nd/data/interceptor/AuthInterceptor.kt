package com.weit2nd.data.interceptor

import com.weit2nd.data.source.auth.AuthDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// TODO 로그인 기능이 추가 되면 에러 핸들링 및 토큰 갱신 기능 추가
class AuthInterceptor @Inject constructor(
    private val dataSource: AuthDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authorization = dataSource.getToken().toString()
        val newRequest = chain.request().newBuilder().apply {
            addHeader(AUTHORIZATION_HEADER, authorization)
        }
        return chain.proceed(newRequest.build())
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "userId"
        private const val BEARER_PREFIX = "Bearer "
    }
}
