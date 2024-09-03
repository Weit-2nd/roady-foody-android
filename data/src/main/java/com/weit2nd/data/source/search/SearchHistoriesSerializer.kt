package com.weit2nd.data.source.search

import androidx.datastore.core.Serializer
import com.weit2nd.data.SearchHistoriesPreferences
import java.io.InputStream
import java.io.OutputStream

object SearchHistoriesSerializer : Serializer<SearchHistoriesPreferences> {
    override val defaultValue: SearchHistoriesPreferences
        get() = SearchHistoriesPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SearchHistoriesPreferences {
        return input.use {
            SearchHistoriesPreferences.parseFrom(it)
        }
    }

    override suspend fun writeTo(
        t: SearchHistoriesPreferences,
        output: OutputStream,
    ) {
        output.use {
            t.writeTo(it)
        }
    }
}
