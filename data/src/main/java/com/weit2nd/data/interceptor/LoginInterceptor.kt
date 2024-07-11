package com.weit2nd.data.interceptor

import com.weit2nd.data.source.auth.AuthDataSource
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class LoginInterceptor @Inject constructor(
    private val dataSource: AuthDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val socialAccessToken = runCatching {
            dataSource.getSocialAccessToken()
        }.getOrElse {
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("${it.message}")
                .body(it.stackTraceToString().toResponseBody())
                .build()
        }
        val authorization = BEARER_PREFIX + socialAccessToken
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
