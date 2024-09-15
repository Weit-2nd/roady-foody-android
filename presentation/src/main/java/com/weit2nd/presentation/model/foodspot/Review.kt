package com.weit2nd.presentation.model.foodspot

import java.time.LocalDateTime

data class Review(
    val reviewId: Long = 0,
    val userId: Long,
    val profileImage: String?,
    val nickname: String,
    val date: LocalDateTime,
    val rating: Float,
    val reviewImages: List<String>,
    val contents: String,
)
