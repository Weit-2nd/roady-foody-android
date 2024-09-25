package com.weit2nd.presentation.ui.mypage.userinfoEdit

sealed class UserInfoEditIntent {
    data object EditUserInfo : UserInfoEditIntent()

    data object NavToBack : UserInfoEditIntent()

    data object ChangeProfileImage : UserInfoEditIntent()

    data class VerifyNickname(
        val nickname: String,
    ) : UserInfoEditIntent()

    data class CheckNicknameDuplication(
        val nickname: String,
    ) : UserInfoEditIntent()

    data class SetNicknameCheckingLoadingState(
        val isLoading: Boolean,
    ) : UserInfoEditIntent()
}
