package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.search.FoodSpot

data class FoodSpotState(
    val id: Long,
    val position: LatLng,
)

fun FoodSpot.toFoodSpotState(): FoodSpotState =
    FoodSpotState(
        id = id,
        position = LatLng.from(latitude, longitude),
    )
