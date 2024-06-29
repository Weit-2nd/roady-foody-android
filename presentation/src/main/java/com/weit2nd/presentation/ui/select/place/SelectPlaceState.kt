package com.weit2nd.presentation.ui.select.place

import com.weit2nd.domain.model.search.Place

data class SelectPlaceState(
    val searchResults: List<Place> = emptyList(),
    val userInput: String = "",
)
