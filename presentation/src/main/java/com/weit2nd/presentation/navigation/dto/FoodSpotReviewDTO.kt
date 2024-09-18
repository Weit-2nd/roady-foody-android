package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodSpotReviewDTO(
    val id: Long = 0,
    val name: String = "",
    val foodSpotsPhotos: List<String> = emptyList(),
    val movableFoodSpots: Boolean,
    val categories: List<FoodCategoryDTO>,
    val reviewCount: Int,
    val averageRating: Float,
    val ratingCounts: List<RatingCountDTO>,
) : Parcelable
