package com.weit2nd.presentation.ui.mypage.foodspot

sealed class FoodSpotHistorySideEffect {
    data object NavToBack : FoodSpotHistorySideEffect()

    data object ShowNetworkErrorMessage : FoodSpotHistorySideEffect()
}
