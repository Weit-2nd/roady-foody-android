package com.weit2nd.data.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import com.weit2nd.domain.model.spot.FoodSpotOperationDayOfWeek
import java.time.DayOfWeek

@Retention
@JsonQualifier
annotation class StringToDayOfWeek

class DayOfWeekConverter {
    @ToJson
    fun toJson(
        @StringToDayOfWeek dayOfWeek: DayOfWeek,
    ): String {
        return FoodSpotOperationDayOfWeek.entries.first { it.dayOfWeek == dayOfWeek }.name
    }

    @FromJson
    @StringToDayOfWeek
    fun fromJson(source: String): DayOfWeek {
        return FoodSpotOperationDayOfWeek.findDayOfWeek(source).dayOfWeek
    }
}
