package com.weit2nd.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Retention
@JsonQualifier
annotation class StringToLocalDateTime

class LocalDateTimeConverter {
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @ToJson
    fun toJson(
        @StringToLocalDateTime localDate: LocalDateTime,
    ): String {
        return dateFormat.format(localDate)
    }

    @FromJson
    @StringToLocalDateTime
    fun fromJson(source: String): LocalDateTime {
        return LocalDateTime.parse(source, dateFormat)
    }
}
