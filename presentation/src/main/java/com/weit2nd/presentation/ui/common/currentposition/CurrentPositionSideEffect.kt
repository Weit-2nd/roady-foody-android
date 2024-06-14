package com.weit2nd.presentation.ui.common.currentposition

import com.kakao.vectormap.LatLng

sealed class CurrentPositionSideEffect {

    data class OnClick(val position: LatLng) : CurrentPositionSideEffect()
}
