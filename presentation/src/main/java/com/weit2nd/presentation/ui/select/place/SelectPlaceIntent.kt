package com.weit2nd.presentation.ui.select.place

import com.weit2nd.domain.model.search.Place

sealed class SelectPlaceIntent {
    data object SearchPlace : SelectPlaceIntent()

    data class StoreSearchWord(
        val input: String,
    ) : SelectPlaceIntent()

    data class SelectPlace(
        val place: Place,
    ) : SelectPlaceIntent()
}
