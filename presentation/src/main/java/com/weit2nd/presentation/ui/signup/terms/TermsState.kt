package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term

data class TermsState(
    val terms: List<Term> = emptyList(),
    val checkedStatus: Map<Term, Boolean> = emptyMap(),
    val agreeAll: Boolean = false,
    val canProceed: Boolean = false,
)
