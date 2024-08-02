package com.weit2nd.presentation.ui.mypage

data class MyPageState(
    val profileImage: String? = null,
    val nickname: String = "",
    val coin: Int = 0,
    val isLogoutDialogShown: Boolean = false,
    val isWithdrawDialogShown: Boolean = false,
)
