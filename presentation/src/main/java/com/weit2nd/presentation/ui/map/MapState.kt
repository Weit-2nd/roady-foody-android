package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap

data class MapState(
    val map: KakaoMap? = null,
    val restaurants: List<RestaurantState> = emptyList(),
)
