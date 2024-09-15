package com.weit2nd.presentation.ui.mypage.review

import com.weit2nd.presentation.model.foodspot.Review

data class ReviewHistoryState(
    val totalCount: Int = 0,
    val reviews: List<Review> = emptyList(),
)
