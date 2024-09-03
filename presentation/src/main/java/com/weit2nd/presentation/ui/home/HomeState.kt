package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.LatLng

data class HomeState(
    val searchWords: String,
    val profileImage: String = "",
    val initialLatLng: LatLng,
)
