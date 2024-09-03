package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng

sealed class HomeIntent {
    data class RequestFoodSpots(
        val centerLat: Double,
        val centerLng: Double,
    ) : HomeIntent()

    data class RefreshMarkers(
        val map: KakaoMap,
    ) : HomeIntent()

    data class RequestCameraMove(
        val position: LatLng,
    ) : HomeIntent()

    data class ShowFoodSpotSummary(
        val foodSpotId: Long,
    ) : HomeIntent()

    data object NavToFoodSpotReport : HomeIntent()

    data object NavToBack : HomeIntent()

    data object NavToSearch : HomeIntent()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : HomeIntent()

    data object NavToMyPage : HomeIntent()

    data object ShowRetryButton : HomeIntent()

    data object SetProfileImage : HomeIntent()
}
