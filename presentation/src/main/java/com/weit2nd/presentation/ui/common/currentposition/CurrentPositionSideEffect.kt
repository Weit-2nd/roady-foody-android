package com.weit2nd.presentation.ui.common.currentposition

import com.kakao.vectormap.LatLng

sealed class CurrentPositionSideEffect {

    data class OnClickSuccess(val position: LatLng) : CurrentPositionSideEffect()
    data class OnClickFail(val error: Throwable) : CurrentPositionSideEffect()
}
