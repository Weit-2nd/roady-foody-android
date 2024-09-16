package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserReviewsUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        userId: Long,
        count: Int,
        lastItemId: Long? = null,
    ): List<UserReview> {
        return repository.getUserReviews(
            userId = userId,
            count = count,
            lastItemId = lastItemId,
        )
    }
}
