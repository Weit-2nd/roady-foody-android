package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.usecase.terms.GetTermsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val getTermsUseCase: GetTermsUseCase
) : BaseViewModel<TermsState, TermsSideEffect>() {

    override val container = container<TermsState, TermsSideEffect>(TermsState())

    init {
        TermsIntent.GetTerms.post()
    }

    fun onCheckedAllAgreeChange(isChecked: Boolean) {
        TermsIntent.SetAllAgreement(isChecked).post()
        TermsIntent.VerifyTermAgreements.post()
    }

    fun onCheckedBoxChange(term: Term, isChecked: Boolean) {
        TermsIntent.SetTermAgreement(term, isChecked).post()
        TermsIntent.UpdateAllAgreementWithTermAgreements.post()
        TermsIntent.VerifyTermAgreements.post()
    }

    fun onDetailBtnClicked(termId: Long) {
        TermsIntent.NavToTermDetail(termId).post()
    }

    private fun TermsIntent.post() = intent {
        when (this@post) {
            TermsIntent.GetTerms -> {
                val terms = getTermsUseCase.invoke()
                reduce {
                    state.copy(
                        termStatuses = terms.map { TermStatus(it) }
                    )
                }
            }

            is TermsIntent.SetAllAgreement -> {
                reduce {
                    state.copy(
                        termStatuses = container.stateFlow.value.termStatuses.map {
                            it.copy(isChecked = isChecked)
                        },
                        agreeAll = isChecked
                    )
                }
            }

            is TermsIntent.SetTermAgreement -> {
                reduce {
                    state.copy(
                        termStatuses = container.stateFlow.value.termStatuses.map {
                            if (it.term == term) it.copy(isChecked = isChecked) else it
                        }
                    )
                }
            }

            TermsIntent.UpdateAllAgreementWithTermAgreements -> {
                if (container.stateFlow.value.termStatuses.all { it.isChecked }) {
                    reduce {
                        state.copy(
                            agreeAll = true
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            agreeAll = false
                        )
                    }
                }
            }

            TermsIntent.VerifyTermAgreements -> {
                val canProceed = container.stateFlow.value.termStatuses
                    .filter { it.term.isRequired }
                    .all { it.isChecked }

                reduce {
                    state.copy(
                        canProceed = canProceed
                    )
                }
            }

            is TermsIntent.NavToTermDetail -> {
                postSideEffect(TermsSideEffect.NavToTermDetail(termId))
            }
        }
    }
}
