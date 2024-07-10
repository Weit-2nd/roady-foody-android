package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class MapIntent {
    data class RequestRestaurants(
        val startLat: Double,
        val startLng: Double,
        val endLat: Double,
        val endLng: Double,
    ) : MapIntent()

    data class RefreshMarkers(
        val map: KakaoMap,
    ) : MapIntent()

    data class RequestCameraMove(
        val position: LatLng,
    ) : MapIntent()
}
