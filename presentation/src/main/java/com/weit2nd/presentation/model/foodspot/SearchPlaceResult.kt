package com.weit2nd.presentation.model.foodspot

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.presentation.util.getDistanceMeter

data class SearchPlaceResult(
    val place: Place,
    val distance: Int,
)

fun Place.toSearchPlaceResult(currentCoordinate: Coordinate) =
    SearchPlaceResult(
        place = this,
        distance = getDistance(currentCoordinate),
    )

private fun Place.getDistance(currentCoordinate: Coordinate) =
    getDistanceMeter(
        start = currentCoordinate,
        end =
            Coordinate(
                latitude = latitude,
                longitude = longitude,
            ),
    )
