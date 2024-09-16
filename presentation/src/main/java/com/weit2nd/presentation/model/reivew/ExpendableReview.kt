package com.weit2nd.presentation.model.reivew

import com.weit2nd.presentation.model.foodspot.Review

data class ExpendableReview(
    val review: Review,
    val isExpended: Boolean,
)
