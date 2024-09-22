package com.weit2nd.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import com.weit2nd.domain.model.Badge

@Retention
@JsonQualifier
annotation class StringToBadge

class BadgeConverter {
    @ToJson
    fun toJson(
        @StringToBadge badge: Badge,
    ): String {
        return when (badge) {
            Badge.BEGINNER -> "초심자"
            Badge.INTERMEDIATE -> "중수"
            Badge.EXPERT -> "고수"
            Badge.SUPER_EXPERT -> "초고수"
            Badge.UNKNOWN -> ""
        }
    }

    @FromJson
    @StringToBadge
    fun fromJson(source: String): Badge {
        return when (source) {
            "초심자" -> Badge.BEGINNER
            "중수" -> Badge.INTERMEDIATE
            "고수" -> Badge.EXPERT
            "초고수" -> Badge.SUPER_EXPERT
            else -> Badge.UNKNOWN
        }
    }
}
