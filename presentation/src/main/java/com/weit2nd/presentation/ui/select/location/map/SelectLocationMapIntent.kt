package com.weit2nd.presentation.ui.select.location.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class SelectLocationMapIntent {

    data class StoreMap(val map: KakaoMap) : SelectLocationMapIntent()

    data class SearchLocation(val coordinate: LatLng?) : SelectLocationMapIntent()

    data class RequestCameraMove(val position: LatLng) : SelectLocationMapIntent()
}
