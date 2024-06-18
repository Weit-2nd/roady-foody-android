package com.weit2nd.presentation.ui.signup

import android.net.Uri

sealed class SignUpIntent {

    data object RequestSignUp : SignUpIntent()
    data object ChangeProfileImage : SignUpIntent()
    data class VerifyNickname(val nickname: String) : SignUpIntent()
    data class CheckNicknameDuplication(val nickname: String) : SignUpIntent()
    data class SetLoadingState(val isLoading: Boolean) : SignUpIntent()
}
