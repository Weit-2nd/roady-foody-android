package com.weit2nd.presentation.ui.signup.terms

import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.usecase.terms.GetTermsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
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
        TermsIntent.AgreeAll(isChecked).post()
    }

    fun onCheckedBoxChange(term: Term, isChecked: Boolean) {
        TermsIntent.AgreeTerm(term, isChecked).post()
        updateAgreeAllWithTermAgreements()
    }

    private fun updateAgreeAllWithTermAgreements() {
        TermsIntent.UpdateAgreeAllWithTermAgreements.post()
    }

    private fun TermsIntent.post() = intent {
        when (this@post) {
            TermsIntent.GetTerms -> {
                val terms = getTermsUseCase.invoke()
                reduce {
                    state.copy(
                        terms = terms,
                        checkedStatus = terms.associateWith { false }
                    )
                }
            }

            is TermsIntent.AgreeAll -> {
                reduce {
                    state.copy(
                        checkedStatus = container.stateFlow.value.terms.associateWith { isChecked },
                        agreeAll = isChecked
                    )
                }
            }

            is TermsIntent.AgreeTerm -> {
                reduce {
                    state.copy(
                        checkedStatus = container.stateFlow.value.checkedStatus.toMutableMap()
                            .apply {
                                this[term] = isChecked
                            },
                    )
                }
            }

            TermsIntent.UpdateAgreeAllWithTermAgreements -> {
                if (container.stateFlow.value.checkedStatus.values.all { it }) {
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
        }
    }
}
