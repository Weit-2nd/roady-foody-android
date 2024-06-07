package com.weit2nd.presentation.ui.signup

import android.net.Uri
import com.weit2nd.domain.model.User

data class SignUpState(
    val user: User,
    val nickname: String = "",
    val profileImageUri: Uri? = null
)
