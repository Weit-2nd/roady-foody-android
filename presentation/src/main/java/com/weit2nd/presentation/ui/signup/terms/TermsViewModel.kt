package com.weit2nd.presentation.ui.signup.terms

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

    private fun TermsIntent.post() = intent {
        when (this@post) {
            TermsIntent.GetTerms -> {
                val items = getTermsUseCase.invoke()
                reduce {
                    state.copy(
                        terms = items
                    )
                }
            }
        }
    }
}
