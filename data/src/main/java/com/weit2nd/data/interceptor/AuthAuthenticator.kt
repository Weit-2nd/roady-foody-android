package com.weit2nd.data.interceptor

import com.weit2nd.data.interceptor.AuthInterceptor.Companion.AUTHORIZATION_HEADER
import com.weit2nd.data.interceptor.AuthInterceptor.Companion.BEARER_PREFIX
import com.weit2nd.data.source.token.TokenDataSource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val dataSource: TokenDataSource,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val mutex = Mutex()
        var request: Request? = null
        runBlocking {
            mutex.withLock {
                val usedToken = response.request.header(AUTHORIZATION_HEADER)
                val lastUpdatedToken = BEARER_PREFIX + dataSource.getAccessToken()
                if (usedToken == lastUpdatedToken) {
                    runCatching {
                        dataSource.refreshAccessToken()
                    }.onSuccess {
                        request = response.request.newBuilder()
                            .header(
                                AUTHORIZATION_HEADER,
                                BEARER_PREFIX + dataSource.getAccessToken()
                            )
                            .build()
                    }
                }
            }
        }
        return request
    }
}
