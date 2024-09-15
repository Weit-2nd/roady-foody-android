package com.weit2nd.data.repository.user

import com.weit2nd.data.model.category.toFoodCategory
import com.weit2nd.data.model.review.toUserReviews
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.spot.FoodSpotHistoryContentDTO
import com.weit2nd.data.model.spot.toFoodSpotPhoto
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.domain.exception.user.UserReviewException
import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource,
) : UserRepository {
    override suspend fun getMyUserInfo(): UserInfo {
        return dataSource.getMyUserInfo().toUser()
    }

    private fun UserDTO.toUser() =
        UserInfo(
            nickname = nickname,
            profileImage = profileImageUrl,
            coin = coin,
        )

    override suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): FoodSpotHistories {
        return dataSource
            .getFoodSpotHistories(
                userId = userId,
                count = count,
                lastItemId = lastItemId,
            ).toFoodSpotHistories()
    }

    override suspend fun getUserReviews(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): List<UserReview> {
        val userReviewsDTO =
            dataSource.getUserReviews(
                userId = userId,
                count = count,
                lastItemId = lastItemId,
            )
        val isReviewEmpty = userReviewsDTO.hasNext.not() && userReviewsDTO.contents.isEmpty()
        return if (isReviewEmpty) {
            throw UserReviewException.NoMoreReviewException()
        } else {
            userReviewsDTO.toUserReviews()
        }
    }

    private fun FoodSpotHistoriesDTO.toFoodSpotHistories() =
        FoodSpotHistories(
            contents = contents.map { it.toFoodSpotHistoryContent() },
            hasNext = hasNext,
        )

    private fun FoodSpotHistoryContentDTO.toFoodSpotHistoryContent() =
        FoodSpotHistoryContent(
            id = id,
            userId = userId,
            foodSpotsId = foodSpotsId,
            name = name,
            longitude = longitude,
            latitude = latitude,
            createdDateTime = createdDateTime,
            reportPhotos = reportPhotos.map { it.toFoodSpotPhoto() },
            categories = categories.map { it.toFoodCategory() },
        )
}
