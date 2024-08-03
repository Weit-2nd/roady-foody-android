package com.weit2nd.presentation.ui.search

sealed interface SearchSideEffect {
    data class ShowToastMessage(
        val message: String,
    ) : SearchSideEffect

    data object NavToBack : SearchSideEffect
}
