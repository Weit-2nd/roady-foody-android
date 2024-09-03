package com.weit2nd.presentation.ui.select.place

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place

sealed class SelectPlaceSideEffect {
    data class SelectPlace(
        val place: Place,
    ) : SelectPlaceSideEffect()

    data class NavToMap(
        val coordinate: Coordinate,
    ) : SelectPlaceSideEffect()

    data object NavToBack : SelectPlaceSideEffect()
}
