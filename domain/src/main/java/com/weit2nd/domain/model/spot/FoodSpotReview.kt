package com.weit2nd.domain.model.spot

import java.time.LocalDateTime

data class FoodSpotReviews(
    val reviews: List<FoodSpotReview>,
    val hasNext: Boolean,
)

data class FoodSpotReview(
    val id: Long,
    val foodSpotsId: Long,
    val userInfo: FoodSpotReviewUserInfo,
    val contents: String,
    val rate: Int,
    val photos: List<FoodSpotPhoto>,
    val createdAt: LocalDateTime,
)

data class FoodSpotReviewUserInfo(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
)

enum class ReviewSortType {
    LATEST,
    HIGHEST,
}
