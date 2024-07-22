package com.weit2nd.presentation.ui.select.place

import com.weit2nd.domain.model.search.Place

sealed class SelectPlaceSideEffect {
    data class SelectPlace(
        val place: Place,
    ) : SelectPlaceSideEffect()
}
