package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

data class HomeState(
    val map: KakaoMap? = null,
    val foodSpots: List<FoodSpotState> = emptyList(),
    val isMoved: Boolean = false,
    val searchWords: String = "",
    val profileImage: String = "",
    val initialLatLng: LatLng = LatLng.from(37.5597706, 126.9423666),
)
