package com.weit2nd.domain.usecase.review

import com.weit2nd.domain.repository.review.ReviewRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val repository: ReviewRepository,
) {
    /**
     * 리뷰를 삭제 합니다. 자신이 작성한 리뷰만 삭제 가능합니다.
     *
     * @param reviewId
     */
    suspend operator fun invoke(reviewId: Long) {
        repository.deleteReview(
            reviewId = reviewId,
        )
    }
}
