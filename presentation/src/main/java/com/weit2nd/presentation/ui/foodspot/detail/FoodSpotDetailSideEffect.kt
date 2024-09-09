package com.weit2nd.presentation.ui.foodspot.detail

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO

sealed interface FoodSpotDetailSideEffect {
    data object NavToBack : FoodSpotDetailSideEffect

    data class MoveAndDrawMarker(
        val map: KakaoMap,
        val position: LatLng,
    ) : FoodSpotDetailSideEffect

    data class NavToPostReview(
        val foodSpotForReviewDTO: FoodSpotForReviewDTO,
    ) : FoodSpotDetailSideEffect
}
