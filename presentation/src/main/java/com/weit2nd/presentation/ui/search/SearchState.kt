package com.weit2nd.presentation.ui.search

import com.weit2nd.domain.model.search.SearchHistory
import com.weit2nd.presentation.model.foodspot.SearchPlaceResult

data class SearchState(
    val searchWords: String = "",
    val searchResults: List<SearchPlaceResult> = emptyList(),
    val histories: List<SearchHistory> = emptyList(),
)
