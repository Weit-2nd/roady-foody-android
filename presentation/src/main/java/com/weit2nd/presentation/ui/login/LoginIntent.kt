package com.weit2nd.presentation.ui.login

sealed class LoginIntent {
    data object RequestLogin : LoginIntent()
}
