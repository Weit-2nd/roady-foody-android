package com.weit2nd.presentation.ui.signup.terms.detail

data class TermDetailState(
    val title: String = "",
    val contents: String = "",
    val isRequired: Boolean = false,
    val isRetryNeeded: Boolean = false,
)
