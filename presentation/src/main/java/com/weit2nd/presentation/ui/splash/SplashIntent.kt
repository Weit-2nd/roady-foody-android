package com.weit2nd.presentation.ui.splash

sealed class SplashIntent {
    data object NavToLogin : SplashIntent()
    data object NavToHome : SplashIntent()
}
