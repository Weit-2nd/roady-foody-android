package com.weit2nd.presentation.ui.signup

import android.net.Uri

data class SignUpState(
    val profileImageUri: Uri? = null,
    val nickname: String = "",
    val isNicknameValid: Boolean = false,
    val isNicknameDuplicate: Boolean = false,
    val warningState: WarningState = WarningState.IS_VALID,
    val canSignUp: Boolean = false,
)

enum class WarningState {
    IS_VALID, IS_NOT_VALID, IS_DUPLICATE
}
