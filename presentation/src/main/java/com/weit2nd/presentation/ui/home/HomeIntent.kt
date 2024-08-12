package com.weit2nd.presentation.ui.home

sealed class HomeIntent {
    data object NavToFoodSpotReport : HomeIntent()

    data object NavToBack : HomeIntent()

    data object NavToSearch : HomeIntent()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : HomeIntent()
}
