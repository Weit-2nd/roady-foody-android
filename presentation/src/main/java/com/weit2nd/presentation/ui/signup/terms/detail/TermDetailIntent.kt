package com.weit2nd.presentation.ui.signup.terms.detail

sealed interface TermDetailIntent {
    data object NavToBack : TermDetailIntent

    data object MissingTermId : TermDetailIntent

    data class LoadTerm(
        val termId: Long,
    ) : TermDetailIntent
}
