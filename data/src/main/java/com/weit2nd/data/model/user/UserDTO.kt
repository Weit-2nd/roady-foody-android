package com.weit2nd.data.model.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.util.StringToBadge
import com.weit2nd.domain.model.Badge

@JsonClass(generateAdapter = true)
data class UserDTO(
    @field:Json(name = "nickname") val nickname: String,
    @field:Json(name = "profileImageUrl") val profileImageUrl: String?,
    @field:Json(name = "coin") val coin: Int,
    @StringToBadge @field:Json(name = "badge") val badge: Badge,
    @field:Json(name = "restDailyReportCreationCount") val restDailyReportCreationCount: Int,
    @field:Json(name = "myRanking") val myRanking: Int,
)
