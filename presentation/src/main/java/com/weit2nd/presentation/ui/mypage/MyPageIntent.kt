package com.weit2nd.presentation.ui.mypage

sealed class MyPageIntent {
    data object GetMyUserInfo : MyPageIntent()

    data class SetLogoutDialogShownState(
        val isShown: Boolean,
    ) : MyPageIntent()

    data class SetWithdrawDialogShownState(
        val isShown: Boolean,
    ) : MyPageIntent()

    data object NavToReviewHistory : MyPageIntent()

    data object NavToFoodSpotHistory : MyPageIntent()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : MyPageIntent()

    data object Logout : MyPageIntent()

    data object Withdraw : MyPageIntent()

    data object NavToBack : MyPageIntent()
}
