package com.weit2nd.presentation.ui.foodspot.report

sealed class FoodSpotReportSideEffect {
    data object NavToSelectPlace : FoodSpotReportSideEffect()

    data class ShowToast(
        val message: String,
    ) : FoodSpotReportSideEffect()

    data object ReportSuccess : FoodSpotReportSideEffect()
}
