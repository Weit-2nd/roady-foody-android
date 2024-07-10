package com.weit2nd.presentation.ui.select.place.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class SelectLocationMapSideEffect {
    data class MoveCamera(
        val map: KakaoMap,
        val position: LatLng,
    ) : SelectLocationMapSideEffect()
}
