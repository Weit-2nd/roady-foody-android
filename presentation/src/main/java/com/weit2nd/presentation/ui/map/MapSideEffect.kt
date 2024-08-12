package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class MapSideEffect {
    data class RefreshMarkers(
        val map: KakaoMap,
        val foodSpotMarkers: List<FoodSpotState>,
    ) : MapSideEffect()

    data class MoveCamera(
        val map: KakaoMap,
        val position: LatLng,
    ) : MapSideEffect()

    data class SendCameraPosition(
        val position: LatLng,
    ) : MapSideEffect()

    data class SendClickedFoodSpotId(
        val foodSpotId: Long,
    ) : MapSideEffect()
}
