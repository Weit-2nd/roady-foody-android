package com.weit2nd.presentation.ui.mypage

import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.presentation.model.foodspot.Review

data class MyPageState(
    val userId: Long = 0,
    val profileImage: String? = null,
    val nickname: String = "",
    val coin: Int = 0,
    val isLogoutDialogShown: Boolean = false,
    val isWithdrawDialogShown: Boolean = false,
    val foodSpotHistory: FoodSpotHistoryContent? = null,
    val review: Review? = null,
)
