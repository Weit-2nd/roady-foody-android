package com.weit2nd.presentation.ui.review

sealed interface PostReviewIntent {
    data object PostReview : PostReviewIntent

    data object PickImage : PostReviewIntent

    data class DeleteImage(
        val image: String,
    ) : PostReviewIntent

    data class ChangeRating(
        val rating: Float,
    ) : PostReviewIntent

    data class ChangeReviewContent(
        val content: String,
    ) : PostReviewIntent
}
