package com.weit2nd.domain.repository.user

import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.model.spot.FoodSpotHistories

interface UserRepository {
    suspend fun getMyUserInfo(): UserInfo

    suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): FoodSpotHistories

    suspend fun getUserReviews(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): List<UserReview>
}
