package com.weit2nd.presentation.ui.select.location

import com.weit2nd.domain.model.Location

data class SelectLocationState(
    val searchResults: List<Location> = emptyList(),
)
