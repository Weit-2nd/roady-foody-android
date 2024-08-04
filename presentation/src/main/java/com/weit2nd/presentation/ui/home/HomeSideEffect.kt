package com.weit2nd.presentation.ui.home

sealed class HomeSideEffect {
    data object NavToFoodSpotReport : HomeSideEffect()

    data class NavToSearch(
        val initialSearchWords: String,
    ) : HomeSideEffect()

    data object NavToBack : HomeSideEffect()
}
