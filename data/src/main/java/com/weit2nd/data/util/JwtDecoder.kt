package com.weit2nd.data.util

import android.util.Base64
import com.google.gson.JsonParseException
import com.squareup.moshi.Moshi
import javax.inject.Inject

class JwtDecoder @Inject constructor(
    private val moshi: Moshi,
) {
    fun <T> decode(
        jwt: String,
        type: Class<T>,
    ): T {
        val splitJwt = jwt.split(".")
        val payload = String(Base64.decode(splitJwt[1], Base64.DEFAULT))
        val adapter = moshi.adapter(type)
        return adapter.fromJson(payload) ?: run {
            throw JsonParseException("올바른 jwt payload가 아님")
        }
    }
}
