package com.weit2nd.presentation.ui.foodspot.report

sealed class FoodSpotReportSideEffect {
    data object NavToSelectPlace : FoodSpotReportSideEffect()
}
