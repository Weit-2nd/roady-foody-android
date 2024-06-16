package com.weit2nd.presentation.ui.select.location.map

import com.kakao.vectormap.KakaoMap
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location

data class SelectLocationMapState(
    val map: KakaoMap? = null,
    val location: Location = Location("", "", Coordinate(0.0, 0.0)),
)
