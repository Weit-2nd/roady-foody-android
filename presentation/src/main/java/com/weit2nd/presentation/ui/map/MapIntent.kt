package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class MapIntent {
    data class RequestFoodSpots(
        val centerLat: Double,
        val centerLng: Double,
    ) : MapIntent()

    data class RefreshMarkers(
        val map: KakaoMap,
    ) : MapIntent()

    data class RequestCameraMove(
        val position: LatLng,
    ) : MapIntent()
}
