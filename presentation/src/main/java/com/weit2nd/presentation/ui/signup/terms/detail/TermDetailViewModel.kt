package com.weit2nd.presentation.ui.signup.terms.detail

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.exception.term.TermIdNotFoundException
import com.weit2nd.domain.usecase.terms.GetTermDetailUseCase
import com.weit2nd.presentation.R
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.TermDetailRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TermDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val termDetailUseCase: GetTermDetailUseCase,
) : BaseViewModel<TermDetailState, TermDetailSideEffect>() {
    override val container: Container<TermDetailState, TermDetailSideEffect> =
        container(TermDetailState())

    private val termId: Long = savedStateHandle[TermDetailRoutes.TERM_ID] ?: -1

    init {
        if (termId != -1L) {
            TermDetailIntent.LoadTerm(termId = termId).post()
        } else {
            TermDetailIntent.MissingTermId.post()
        }
    }

    fun onRetryClick() {
        TermDetailIntent.LoadTerm(termId = termId).post()
    }

    fun onNavigateClick() {
        TermDetailIntent.NavToBack.post()
    }

    private fun TermDetailIntent.post() =
        intent {
            when (this@post) {
                is TermDetailIntent.LoadTerm -> {
                    reduce {
                        state.copy(
                            isRetryNeeded = false,
                        )
                    }
                    runCatching {
                        termDetailUseCase(termId)
                    }.onSuccess { termDetail ->
                        reduce {
                            state.copy(
                                title = termDetail.title,
                                contents = termDetail.content,
                                isRequired = termDetail.isRequired,
                            )
                        }
                    }.onFailure {
                        if (it is TermIdNotFoundException) {
                            postSideEffect(TermDetailSideEffect.ShowToast(R.string.term_detail_id_not_found))
                            postSideEffect(TermDetailSideEffect.NavToBack)
                        } else {
                            postSideEffect(TermDetailSideEffect.ShowToast(R.string.term_detail_network_error))
                            reduce {
                                state.copy(
                                    isRetryNeeded = true,
                                )
                            }
                        }
                    }
                }
                TermDetailIntent.NavToBack -> {
                    postSideEffect(TermDetailSideEffect.NavToBack)
                }
                TermDetailIntent.MissingTermId -> {
                    postSideEffect(TermDetailSideEffect.ShowToast(R.string.term_detail_id_not_found))
                    postSideEffect(TermDetailSideEffect.NavToBack)
                }
            }
        }
}
