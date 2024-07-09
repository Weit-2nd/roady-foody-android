package com.weit2nd.presentation.ui.splash

sealed class SplashIntent {
    data object RequestLogin : SplashIntent()
}
