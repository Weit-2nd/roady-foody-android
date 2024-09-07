package com.weit2nd.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Retention
@JsonQualifier
annotation class StringToLocalTime

class LocalTimeConverter {
    private val dateFormat = DateTimeFormatter.ofPattern("HH:mm")

    @ToJson
    fun toJson(
        @StringToLocalTime localTime: LocalTime,
    ): String {
        return dateFormat.format(localTime)
    }

    @FromJson
    @StringToLocalTime
    fun fromJson(source: String): LocalTime {
        return LocalTime.parse(source, dateFormat)
    }
}
