package com.weit2nd.presentation.ui.foodspot.review

import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO

sealed interface FoodSpotReviewSideEffect {
    data class NavToPostReview(
        val foodSpotForReviewDTO: FoodSpotForReviewDTO,
    ) : FoodSpotReviewSideEffect

    data object ShowNetworkErrorMessage : FoodSpotReviewSideEffect
}
