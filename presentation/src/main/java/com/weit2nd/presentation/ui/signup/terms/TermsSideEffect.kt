package com.weit2nd.presentation.ui.signup.terms

sealed class TermsSideEffect {

    data class NavToTermDetail(val termId: Long) : TermsSideEffect()
    data class NavToSignUp(val termIds: List<Long>) : TermsSideEffect()
}
