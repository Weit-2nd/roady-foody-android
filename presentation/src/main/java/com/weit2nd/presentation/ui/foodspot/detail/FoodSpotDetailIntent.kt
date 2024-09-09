package com.weit2nd.presentation.ui.foodspot.detail

import com.kakao.vectormap.KakaoMap

sealed interface FoodSpotDetailIntent {
    data class LoadFoodSpotDetail(
        val id: Long,
    ) : FoodSpotDetailIntent

    data class ChangeOperationHoursOpenState(
        val updatedState: Boolean,
    ) : FoodSpotDetailIntent

    data object NavToBack : FoodSpotDetailIntent

    data class OnMapReady(
        val map: KakaoMap,
    ) : FoodSpotDetailIntent

    data object NavToPostReview : FoodSpotDetailIntent

    data class ChangeReviewContentExpendState(
        val position: Int,
        val expandState: Boolean,
    ) : FoodSpotDetailIntent
}
