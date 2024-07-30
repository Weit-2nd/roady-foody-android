package com.weit2nd.presentation.ui.review

data class PostReviewState(
    val foodSpotName: String = "",
    val rating: Float = 5f,
    val selectedImages: List<String> = emptyList(),
    val content: String = "",
    val maxLength: Int = 0,
)
