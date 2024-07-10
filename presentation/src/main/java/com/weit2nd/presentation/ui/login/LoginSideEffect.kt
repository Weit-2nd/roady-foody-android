package com.weit2nd.presentation.ui.login

import com.weit2nd.domain.model.User

sealed class LoginSideEffect {
    data class NavToHome(
        val user: User,
    ) : LoginSideEffect()

    data object NavToTermAgreement : LoginSideEffect()
}
