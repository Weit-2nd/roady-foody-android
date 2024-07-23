package com.weit2nd.presentation.ui.select.place.map

import androidx.compose.ui.unit.IntOffset
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.search.Place

sealed class SelectLocationMapIntent {
    data class StoreMap(
        val map: KakaoMap,
    ) : SelectLocationMapIntent()

    data class StoreSelectMarkerOffset(
        val offset: IntOffset,
    ) : SelectLocationMapIntent()

    data object StartLocatingMap : SelectLocationMapIntent()

    data class SearchLocation(
        val coordinate: LatLng?,
    ) : SelectLocationMapIntent()

    data class RequestCameraMove(
        val position: LatLng,
    ) : SelectLocationMapIntent()

    data class SelectPlace(
        val place: Place,
    ) : SelectLocationMapIntent()
}
