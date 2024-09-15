package com.weit2nd.domain.model.review

import com.weit2nd.domain.model.spot.FoodSpotPhoto
import java.time.LocalDateTime

data class UserReview(
    val id: Long,
    val contents: String,
    val rating: Int,
    val photos: List<FoodSpotPhoto>,
    val createdAt: LocalDateTime,
)
