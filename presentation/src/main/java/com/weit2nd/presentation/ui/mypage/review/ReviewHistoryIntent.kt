package com.weit2nd.presentation.ui.mypage.review

sealed interface ReviewHistoryIntent {
    data object NavToBack : ReviewHistoryIntent

    data class LoadNextReviews(
        val lastId: Long?,
    ) : ReviewHistoryIntent

    data class ChangeReviewContentExpendState(
        val position: Int,
        val expandState: Boolean,
    ) : ReviewHistoryIntent
}
