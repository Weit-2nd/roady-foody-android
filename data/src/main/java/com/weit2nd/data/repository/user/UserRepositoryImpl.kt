package com.weit2nd.data.repository.user

import com.weit2nd.data.model.category.toFoodCategory
import com.weit2nd.data.model.review.toUserReviews
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.spot.FoodSpotHistoryContentDTO
import com.weit2nd.data.model.spot.toFoodSpotPhoto
import com.weit2nd.data.model.user.TokenPayload
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.data.util.JwtDecoder
import com.weit2nd.domain.exception.user.UserReviewException
import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenDataSource: TokenDataSource,
    private val jwtDecoder: JwtDecoder,
) : UserRepository {
    override suspend fun getMyUserInfo(): UserInfo {
        return userDataSource.getMyUserInfo().toUser()
    }

    private suspend fun UserDTO.toUser(): UserInfo =
        UserInfo(
            userId = getUserId(),
            nickname = nickname,
            profileImage = profileImageUrl,
            coin = coin,
        )

    private suspend fun getUserId(): Long {
        val token =
            tokenDataSource.getAccessToken()?.token ?: run {
                throw RuntimeException("유저 토큰이 존재하지 않음")
            }
        return jwtDecoder.decode(token, TokenPayload::class.java).userId
    }

    override suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): FoodSpotHistories {
        return userDataSource
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
            userDataSource.getUserReviews(
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
