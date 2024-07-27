package com.weit2nd.data.model.review

data class PostReviewRequest(
    val foodSpotId: Long,
    val contents: String,
    val rating: Int,
)
