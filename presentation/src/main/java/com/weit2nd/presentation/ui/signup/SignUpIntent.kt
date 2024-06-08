package com.weit2nd.presentation.ui.signup

import android.net.Uri

sealed class SignUpIntent {

    data object RequestSignUp : SignUpIntent()
    data class SetProfileImage(val imageUri: Uri?) : SignUpIntent()
    data class VerifyNickname(val nickname: String) : SignUpIntent()
}
