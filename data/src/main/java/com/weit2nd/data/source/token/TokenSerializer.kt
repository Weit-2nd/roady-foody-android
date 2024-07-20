package com.weit2nd.data.source.token

import androidx.datastore.core.Serializer
import com.weit2nd.data.TokenPreferences
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<TokenPreferences> {
    override val defaultValue: TokenPreferences
        get() = TokenPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenPreferences {
        return input.use {
            TokenPreferences.parseFrom(it)
        }
    }

    override suspend fun writeTo(
        t: TokenPreferences,
        output: OutputStream,
    ) {
        output.use {
            t.writeTo(it)
        }
    }
}
