package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap

sealed class MapSideEffect {

    data class RefreshMarkers(
        val map: KakaoMap,
        val restaurantMarkers: List<RestaurantState>
    ) : MapSideEffect()
}
