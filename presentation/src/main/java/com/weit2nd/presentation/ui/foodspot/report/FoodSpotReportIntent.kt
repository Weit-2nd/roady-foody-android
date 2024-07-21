package com.weit2nd.presentation.ui.foodspot.report

sealed class FoodSpotReportIntent {
    data object GetFoodSpotCategories : FoodSpotReportIntent()

    data class ChangeFoodTruckState(
        val isFoodTruck: Boolean,
    ) : FoodSpotReportIntent()

    data class ChangeOpenState(
        val isOpen: Boolean,
    ) : FoodSpotReportIntent()

    data class ChangeCategoryStatus(
        val categoryStatus: CategoryStatus,
    ) : FoodSpotReportIntent()

    data object SelectImage : FoodSpotReportIntent()

    data class DeleteImage(
        val imgUri: String,
    ) : FoodSpotReportIntent()
}
