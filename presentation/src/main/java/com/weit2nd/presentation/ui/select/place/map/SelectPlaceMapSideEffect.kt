package com.weit2nd.presentation.ui.select.place.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.search.Place

sealed class SelectPlaceMapSideEffect {
    data class MoveCamera(
        val map: KakaoMap,
        val position: LatLng,
    ) : SelectPlaceMapSideEffect()

    data class SelectPlace(
        val place: Place,
    ) : SelectPlaceMapSideEffect()

    data class ShowToast(
        val message: String,
    ) : SelectPlaceMapSideEffect()
}
