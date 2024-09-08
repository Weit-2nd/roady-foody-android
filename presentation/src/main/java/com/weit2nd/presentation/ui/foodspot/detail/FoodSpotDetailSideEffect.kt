package com.weit2nd.presentation.ui.foodspot.detail

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed interface FoodSpotDetailSideEffect {
    data object NavToBack : FoodSpotDetailSideEffect

    data class MoveAndDrawMarker(
        val map: KakaoMap,
        val position: LatLng,
    ) : FoodSpotDetailSideEffect
}
