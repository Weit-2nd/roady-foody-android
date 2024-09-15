package com.weit2nd.presentation.ui.mypage.review

import com.weit2nd.presentation.model.reivew.ExpendableReview

data class ReviewHistoryState(
    val totalCount: Int = 0,
    val reviews: List<ExpendableReview> = emptyList(),
)
