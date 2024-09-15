package com.weit2nd.presentation.ui.mypage

sealed class MyPageSideEffect {
    data class ShowToastMessage(
        val message: String,
    ) : MyPageSideEffect()

    data object NavToLogin : MyPageSideEffect()

    data object NavToBack : MyPageSideEffect()
}
