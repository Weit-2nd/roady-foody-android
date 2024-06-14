package com.weit2nd.presentation.ui.common.currentposition

sealed class CurrentPositionIntent {

    data object RequestCurrentPosition : CurrentPositionIntent()
    data class SetLoadingState(val isLoading: Boolean) : CurrentPositionIntent()
}
