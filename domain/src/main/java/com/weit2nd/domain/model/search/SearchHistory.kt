package com.weit2nd.domain.model.search

import com.weit2nd.domain.model.Coordinate

data class SearchHistory(
    val words: String,
    val coordinate: Coordinate,
    val isPlace: Boolean,
)
