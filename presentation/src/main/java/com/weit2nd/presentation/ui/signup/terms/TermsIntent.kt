package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term

sealed class TermsIntent {

    data object GetTerms : TermsIntent()
    data class AgreeAll(val isChecked: Boolean) : TermsIntent()
    data class AgreeTerm(val term: Term, val isChecked: Boolean) : TermsIntent()
    data object UpdateAgreeAllWithTermAgreements : TermsIntent()
}
