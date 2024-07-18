package com.weit2nd.presentation.ui.signup

sealed class SignUpIntent {
    data object RequestSignUp : SignUpIntent()

    data object ChangeProfileImage : SignUpIntent()

    data class VerifyNickname(
        val nickname: String,
    ) : SignUpIntent()

    data class CheckNicknameDuplication(
        val nickname: String,
    ) : SignUpIntent()

    data class SetNicknameCheckingLoadingState(
        val isLoading: Boolean,
    ) : SignUpIntent()
}
