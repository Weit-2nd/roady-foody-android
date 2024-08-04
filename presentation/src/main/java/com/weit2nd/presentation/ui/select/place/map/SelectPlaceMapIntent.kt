package com.weit2nd.presentation.ui.select.place.map

import androidx.compose.ui.unit.IntOffset
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.search.Place

sealed class SelectPlaceMapIntent {
    data class StoreMap(
        val map: KakaoMap,
    ) : SelectPlaceMapIntent()

    data class StoreSelectMarkerOffset(
        val offset: IntOffset,
    ) : SelectPlaceMapIntent()

    data object StartPlaceMap : SelectPlaceMapIntent()

    data class SearchPlace(
        val coordinate: LatLng?,
    ) : SelectPlaceMapIntent()

    data class RequestCameraMove(
        val position: LatLng,
    ) : SelectPlaceMapIntent()

    data class SelectPlace(
        val place: Place,
    ) : SelectPlaceMapIntent()
}
