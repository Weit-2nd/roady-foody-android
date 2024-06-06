package com.weit2nd.presentation.ui.signup

sealed class SignUpIntent {

    data object RequestSignUp : SignUpIntent()
}
