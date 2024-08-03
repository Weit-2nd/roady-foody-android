package com.weit2nd.presentation.ui.signup

sealed class SignUpSideEffect {
    data object NavToHome : SignUpSideEffect()

    data class ShowToast(
        val message: String,
    ) : SignUpSideEffect()
}
