package com.weit2nd.presentation.ui.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.SearchHistory

sealed interface SearchIntent {
    data object GetSearchHistory : SearchIntent

    data class ChangeSearchWords(
        val searchWords: String,
    ) : SearchIntent

    data class RemoveHistory(
        val history: SearchHistory,
    ) : SearchIntent

    data class SearchWithWords(
        val words: String,
    ) : SearchIntent

    data class SearchWithPlace(
        val name: String,
        val coordinate: Coordinate,
    ) : SearchIntent

    data object NavToBack : SearchIntent
}
