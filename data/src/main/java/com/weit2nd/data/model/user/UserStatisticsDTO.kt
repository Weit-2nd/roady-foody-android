package com.weit2nd.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.domain.model.user.UserStatistics

@JsonClass(generateAdapter = true)
data class UserStatisticsDTO(
    @field:Json(name = "reportCount") val reportCount: Int,
    @field:Json(name = "reviewCount") val reviewCount: Int,
    @field:Json(name = "likeCount") val likeCount: Int,
)

fun UserStatisticsDTO.toUserStatistics() =
    UserStatistics(
        reportCount = reportCount,
        reviewCount = reviewCount,
        likeCount = likeCount,
    )
