package com.weit2nd.presentation.ui.signup.terms

sealed class TermsSideEffect {

    data class NavToTermDetail(val termId: Long) : TermsSideEffect()
}
