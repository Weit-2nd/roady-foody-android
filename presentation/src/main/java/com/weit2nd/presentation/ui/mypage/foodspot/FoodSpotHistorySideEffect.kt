package com.weit2nd.presentation.ui.mypage.foodspot

sealed class FoodSpotHistorySideEffect {
    data object NavToBack : FoodSpotHistorySideEffect()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : FoodSpotHistorySideEffect()

    data object ShowNetworkErrorMessage : FoodSpotHistorySideEffect()
}
