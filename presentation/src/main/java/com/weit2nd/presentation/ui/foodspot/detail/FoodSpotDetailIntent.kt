package com.weit2nd.presentation.ui.foodspot.detail

sealed interface FoodSpotDetailIntent {
    data class LoadFoodSpotDetail(
        val id: Long,
    ) : FoodSpotDetailIntent

    data class ChangeOperationHoursOpenState(
        val updatedState: Boolean,
    ) : FoodSpotDetailIntent
}
