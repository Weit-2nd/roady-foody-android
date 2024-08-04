package com.weit2nd.presentation.ui.select.place.map

import androidx.compose.ui.unit.IntOffset
import com.kakao.vectormap.KakaoMap
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place

data class SelectLocationMapState(
    val map: KakaoMap? = null,
    val selectMarkerOffset: IntOffset = IntOffset.Zero,
    val place: Place =
        Place(
            placeName = "",
            addressName = "",
            roadAddressName = "",
            longitude = 0.0,
            latitude = 0.0,
            tel = "",
        ),
    val isLoading: Boolean = false,
    val initialPosition: Coordinate = Coordinate(37.5597706, 126.9423666),
)
