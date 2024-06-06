package com.weit2nd.presentation.ui.signup

import com.weit2nd.domain.model.User

data class SignUpState(
    val user: User,
    val nickname: String = ""
)
