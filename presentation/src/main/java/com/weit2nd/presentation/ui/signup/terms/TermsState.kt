package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term

data class TermsState(
    val terms: List<Term> = emptyList(),
    val checkedStatus: List<Boolean> = emptyList(),
)
