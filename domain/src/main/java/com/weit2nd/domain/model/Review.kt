package com.weit2nd.domain.model

import java.time.LocalDateTime

data class Review(
    val userId: Long,
    val profileImage: String,
    val nickname: String,
    val date: LocalDateTime,
    val rating: Float,
    val reviewImages: List<String>,
    val reviewDetail: String,
)
