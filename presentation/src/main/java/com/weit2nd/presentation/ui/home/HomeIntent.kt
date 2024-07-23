package com.weit2nd.presentation.ui.home

sealed class HomeIntent {
    data object NavToFoodSpotReport : HomeIntent()
}
