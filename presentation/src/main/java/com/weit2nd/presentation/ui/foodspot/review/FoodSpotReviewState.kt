package com.weit2nd.presentation.ui.foodspot.review

import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.RatingCount
import com.weit2nd.presentation.model.reivew.ExpendableReview

data class FoodSpotReviewState(
    val id: Long = 0,
    val name: String = "",
    val foodSpotsPhotos: List<String> = emptyList(),
    val movableFoodSpots: Boolean = false,
    val categories: List<FoodCategory> = emptyList(),
    val reviewCount: Int = 0,
    val averageRating: Float = 0f,
    val reviews: List<ExpendableReview> = emptyList(),
    val ratingCounts: List<RatingCount> = emptyList(),
)
