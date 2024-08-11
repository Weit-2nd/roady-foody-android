package com.weit2nd.presentation.ui.home

import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO

sealed class HomeSideEffect {
    data object NavToFoodSpotReport : HomeSideEffect()

    data class NavToSearch(
        val placeSearchDTO: PlaceSearchDTO,
    ) : HomeSideEffect()

    data object NavToBack : HomeSideEffect()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : HomeSideEffect()
}
