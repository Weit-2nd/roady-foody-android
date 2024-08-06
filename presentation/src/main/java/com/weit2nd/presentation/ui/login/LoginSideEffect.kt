package com.weit2nd.presentation.ui.login

sealed class LoginSideEffect {
    data object NavToHome : LoginSideEffect()

    data object NavToTermAgreement : LoginSideEffect()
}
