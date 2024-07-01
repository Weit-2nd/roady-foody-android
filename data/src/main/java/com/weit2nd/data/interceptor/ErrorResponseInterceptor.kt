package com.weit2nd.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import com.squareup.moshi.Moshi
import com.weit2nd.data.model.ErrorResponse
import java.io.IOException
import javax.inject.Inject

class ErrorResponseInterceptor @Inject constructor(
    moshi: Moshi,
) : Interceptor {
    private val errorAdapter = moshi.adapter(ErrorResponse::class.java)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.peekBody(Long.MAX_VALUE).string()
            val errorResponse: ErrorResponse? = errorAdapter.fromJson(errorBody)
            val responseWithErrorMessage = response.newBuilder()
                .message(errorResponse?.errorMessage ?: "NoErrorMessage")
                .build()
            return responseWithErrorMessage
        }

        return response
    }
}
