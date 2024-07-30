package com.weit2nd.presentation.ui.review

sealed interface PostReviewSideEffect {
    data class ShowToast(
        val message: String,
    ) : PostReviewSideEffect

    data object NavToBack : PostReviewSideEffect
}
