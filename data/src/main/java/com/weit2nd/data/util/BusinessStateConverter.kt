package com.weit2nd.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import com.weit2nd.domain.model.search.BusinessState

@Retention
@JsonQualifier
annotation class StringToBusinessState

class BusinessStateConverter {
    @ToJson
    fun toJson(
        @StringToBusinessState businessState: BusinessState,
    ): String {
        return businessState.name
    }

    @FromJson
    @StringToBusinessState
    fun fromJson(source: String): BusinessState {
        return BusinessState.valueOf(source)
    }
}
