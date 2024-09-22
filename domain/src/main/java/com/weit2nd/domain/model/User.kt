package com.weit2nd.domain.model

data class UserInfo(
    val userId: Long,
    val nickname: String,
    val profileImage: String?,
    val coin: Int,
    val badge: Badge,
    val restDailyReportCreationCount: Int,
    val myRanking: Int,
)
