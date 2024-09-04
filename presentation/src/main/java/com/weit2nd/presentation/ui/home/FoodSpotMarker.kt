package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.search.FoodSpot

data class FoodSpotMarker(
    val id: Long,
    val position: LatLng,
    val isSelected: Boolean,
)

fun FoodSpot.toFoodSpotMarker(): FoodSpotMarker =
    FoodSpotMarker(
        id = id,
        position = LatLng.from(latitude, longitude),
        isSelected = false,
    )
