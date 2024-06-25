package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term

data class TermsState(
    val termStatuses: List<TermStatus> = emptyList(),
    val agreeAll: Boolean = false,
    val canProceed: Boolean = false,
)

data class TermStatus(
    val term: Term,
    val isChecked: Boolean = false,
)
