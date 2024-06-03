package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Restaurant

data class RestaurantState(
    val id: Long,
    val coordinate: LatLng
)

fun Restaurant.toRestaurantState(): RestaurantState = RestaurantState(
    id = id, coordinate = LatLng.from(y, x)
)
