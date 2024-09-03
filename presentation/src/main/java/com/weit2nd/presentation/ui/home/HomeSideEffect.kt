package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO

sealed class HomeSideEffect {
    data class RefreshMarkers(
        val map: KakaoMap,
        val foodSpotMarkers: List<FoodSpotState>,
    ) : HomeSideEffect()

    data object NavToFoodSpotReport : HomeSideEffect()

    data class NavToSearch(
        val placeSearchDTO: PlaceSearchDTO,
    ) : HomeSideEffect()

    data object NavToBack : HomeSideEffect()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : HomeSideEffect()

    data object NavToMyPage : HomeSideEffect()

    data class MoveCamera(
        val map: KakaoMap,
        val position: LatLng,
    ) : HomeSideEffect()
}
