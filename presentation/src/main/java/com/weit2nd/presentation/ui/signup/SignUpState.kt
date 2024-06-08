package com.weit2nd.presentation.ui.signup

import android.net.Uri
import com.weit2nd.domain.model.User

data class SignUpState(
    val user: User,
    val profileImageUri: Uri? = null,
    val nickname: String = "",
    val isNicknameValid: Boolean = true,
    val isNicknameDuplicate: Boolean = false,
    val canSignUp: Boolean = false,
)
