package com.weit2nd.presentation.ui.common.currentposition

sealed class CurrentPositionIntent {

    data object RequestCurrentPosition : CurrentPositionIntent()
}
