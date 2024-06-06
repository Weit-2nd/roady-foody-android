package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap

sealed class MapIntent {
    data class RequestRestaurants(
        val startLat: Double,
        val startLng: Double,
        val endLat: Double,
        val endLng: Double
    ) : MapIntent()

    data class RefreshMarkers(
        val map: KakaoMap,
    ) : MapIntent()
}
