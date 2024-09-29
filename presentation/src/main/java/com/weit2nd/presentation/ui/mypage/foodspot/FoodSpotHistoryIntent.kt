package com.weit2nd.presentation.ui.mypage.foodspot

sealed class FoodSpotHistoryIntent {
    data object NavToBack : FoodSpotHistoryIntent()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : FoodSpotHistoryIntent()

    data class LoadNextFoodSpots(
        val lastId: Long?,
    ) : FoodSpotHistoryIntent()

    data object InitTotalCount : FoodSpotHistoryIntent()
}
