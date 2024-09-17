package com.weit2nd.presentation.ui.foodspot.review

sealed interface FoodSpotReviewIntent {
    data object NavToPostReview : FoodSpotReviewIntent

    data class LoadNextReviews(
        val lastId: Long?,
    ) : FoodSpotReviewIntent

    data class ChangeReviewContentExpendState(
        val position: Int,
        val expandState: Boolean,
    ) : FoodSpotReviewIntent
}
