package com.weit2nd.presentation.ui.signup.terms.detail

import androidx.annotation.StringRes

sealed interface TermDetailSideEffect {

    data object NavToBack : TermDetailSideEffect

    data class ShowToast(
        @StringRes val messageRes: Int,
    ) : TermDetailSideEffect

}
