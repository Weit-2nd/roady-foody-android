package com.weit2nd.presentation.ui.common.currentposition

import com.kakao.vectormap.LatLng

sealed class CurrentPositionSideEffect {
    data class PositionRequestSuccess(
        val position: LatLng,
    ) : CurrentPositionSideEffect()

    data class PositionRequestFailed(
        val error: Throwable,
    ) : CurrentPositionSideEffect()
}
