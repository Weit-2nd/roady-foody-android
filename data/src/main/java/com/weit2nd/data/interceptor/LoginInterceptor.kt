package com.weit2nd.data.interceptor

import com.weit2nd.data.source.auth.AuthDataSource
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import javax.inject.Inject

class LoginInterceptor @Inject constructor(
    private val dataSource: AuthDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runCatching {
            dataSource.getSocialAccessToken()
        }.fold(
            onSuccess = { socialAccessToken ->
                val authorization = BEARER_PREFIX + socialAccessToken
                val newRequest =
                    chain.request().newBuilder().apply {
                        addHeader(AUTHORIZATION_HEADER, authorization)
                    }
                chain.proceed(newRequest.build())
            },
            onFailure = {
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(HTTP_UNAUTHORIZED)
                    .message("${it.message}")
                    .body(it.stackTraceToString().toResponseBody())
                    .build()
            }
        )
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }
}
