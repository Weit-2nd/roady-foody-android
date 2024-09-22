package com.weit2nd.presentation.ui.mypage.userinfoEdit

sealed class UserInfoEditSideEffect {
    data object NavToBack : UserInfoEditSideEffect()

    data class ShowToast(
        val message: String,
    ) : UserInfoEditSideEffect()
}
