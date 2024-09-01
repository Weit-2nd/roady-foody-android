package com.weit2nd.presentation.ui.search

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.SearchHistory

data class SearchState(
    val searchWords: String = "",
    val searchResults: List<Place> = emptyList(),
    val histories: List<SearchHistory> = emptyList(),
)
