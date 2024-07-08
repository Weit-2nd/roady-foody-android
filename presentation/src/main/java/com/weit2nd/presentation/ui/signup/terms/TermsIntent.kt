package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term

sealed class TermsIntent {

    data object GetTerms : TermsIntent()
    data class SetAllAgreement(val isChecked: Boolean) : TermsIntent()
    data class SetTermAgreement(val term: Term, val isChecked: Boolean) : TermsIntent()
    data object UpdateAllAgreementWithTermAgreements : TermsIntent()
    data object VerifyTermAgreements : TermsIntent()
    data class NavToTermDetail(val termId: Long) : TermsIntent()
    data object NavToSignUp : TermsIntent()
}
