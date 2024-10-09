package com.weit2nd.presentation.ui.mypage

import com.weit2nd.domain.model.Badge
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.presentation.model.foodspot.Review

data class MyPageState(
    val isLoading: Boolean = false,
    val userId: Long = 0,
    val profileImage: String? = null,
    val nickname: String = "",
    val coin: Int = 0,
    val badge: Badge = Badge.UNKNOWN,
    val restDailyReportCreationCount: Int = 0,
    val myRanking: Int = 0,
    val isRankingDialogShown: Boolean = false,
    val isLogoutDialogShown: Boolean = false,
    val isWithdrawDialogShown: Boolean = false,
    val foodSpotHistory: FoodSpotHistoryContent? = null,
    val foodSpotCount: Int = 0,
    val review: Review? = null,
    val reviewCount: Int = 0,
    val likeCount: Int = 0,
)
