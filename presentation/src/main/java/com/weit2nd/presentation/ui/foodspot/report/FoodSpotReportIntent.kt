package com.weit2nd.presentation.ui.foodspot.report

sealed class FoodSpotReportIntent {
    data class ChangeFoodTruckState(
        val isFoodTruck: Boolean,
    ) : FoodSpotReportIntent()
}
