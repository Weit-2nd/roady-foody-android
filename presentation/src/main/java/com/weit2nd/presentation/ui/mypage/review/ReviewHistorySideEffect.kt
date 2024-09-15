package com.weit2nd.presentation.ui.mypage.review

sealed interface ReviewHistorySideEffect {
    data object NavToBack : ReviewHistorySideEffect

    data object ShowNetworkErrorMessage : ReviewHistorySideEffect
}
