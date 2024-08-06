package com.weit2nd.presentation.ui.search

import com.weit2nd.domain.model.search.Place

sealed interface SearchIntent {
    data object GetSearchHistory : SearchIntent

    data class ChangeSearchWords(
        val searchWords: String,
    ) : SearchIntent

    data class RemoveHistory(
        val history: String,
    ) : SearchIntent

    data class SearchWithWords(
        val words: String,
    ) : SearchIntent

    data class SearchWithPlace(
        val place: Place,
    ) : SearchIntent

    data object NavToBack : SearchIntent
}
