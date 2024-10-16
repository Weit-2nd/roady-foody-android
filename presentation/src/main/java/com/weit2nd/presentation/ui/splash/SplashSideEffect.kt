package com.weit2nd.presentation.ui.splash

sealed class SplashSideEffect {
    data object NavToLogin : SplashSideEffect()

    data object NavToHome : SplashSideEffect()

    data class ShowToast(
        val message: String,
    ) : SplashSideEffect()
}
