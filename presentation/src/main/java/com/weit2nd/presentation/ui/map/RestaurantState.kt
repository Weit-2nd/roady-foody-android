package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Restaurant

data class RestaurantState(
    val id: Long,
    val position: LatLng
)

fun Restaurant.toRestaurantState(): RestaurantState = RestaurantState(
    id = id, position = LatLng.from(latitude, longitude)
)
