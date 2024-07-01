package com.weit2nd.presentation.ui.signup

import com.weit2nd.domain.model.User

sealed class SignUpSideEffect {
    data class NavToHome(
        val user: User,
    ) : SignUpSideEffect()

    data class ShowToast(
        val message: String,
    ) : SignUpSideEffect()
}
