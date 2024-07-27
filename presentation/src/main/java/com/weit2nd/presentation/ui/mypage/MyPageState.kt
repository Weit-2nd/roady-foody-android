package com.weit2nd.presentation.ui.mypage

data class MyPageState(
    val profileImage: String = "",
    val nickname: String = "",
    val isLogoutDialogShown: Boolean = false,
    val isWithdrawDialogShown: Boolean = false,
)
