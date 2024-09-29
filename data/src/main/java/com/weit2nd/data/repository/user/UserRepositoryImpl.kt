package com.weit2nd.data.repository.user

import com.weit2nd.data.model.category.toFoodCategory
import com.weit2nd.data.model.review.toUserReviews
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.spot.FoodSpotHistoryContentDTO
import com.weit2nd.data.model.spot.toFoodSpotPhoto
import com.weit2nd.data.model.user.TokenPayload
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.model.user.UserNicknameRequest
import com.weit2nd.data.model.user.toUserStatistics
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.data.util.JwtDecoder
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.exception.user.UserFoodSpotException
import com.weit2nd.domain.exception.user.UserInfoEditException
import com.weit2nd.domain.exception.user.UserReviewException
import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.model.user.UserStatistics
import com.weit2nd.domain.repository.user.UserRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_CONFLICT
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenDataSource: TokenDataSource,
    private val jwtDecoder: JwtDecoder,
    private val localImageDatasource: LocalImageDatasource,
) : UserRepository {
    override suspend fun getMyUserInfo(): UserInfo {
        return userDataSource.getMyUserInfo().toUser()
    }

    private suspend fun UserDTO.toUser(): UserInfo =
        UserInfo(
            userId = getMyUserId(),
            nickname = nickname,
            profileImage = profileImageUrl,
            coin = coin,
            badge = badge,
            restDailyReportCreationCount = restDailyReportCreationCount,
            myRanking = myRanking,
        )

    override suspend fun getMyUserId(): Long {
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
        val foodSpots =
            userDataSource
                .getFoodSpotHistories(
                    userId = userId,
                    count = count,
                    lastItemId = lastItemId,
                ).toFoodSpotHistories()

        val isFoodSpotEmpty = foodSpots.hasNext.not() && foodSpots.contents.isEmpty()
        return if (isFoodSpotEmpty) {
            throw UserFoodSpotException.NoMoreFoodSpotException()
        } else {
            foodSpots
        }
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
            isFoodTruck = isFoodTruck,
        )

    override suspend fun editUserInfo(profileImage: String?) {
        val imagePart =
            profileImage?.let { imageUri ->
                if (localImageDatasource.checkImageUriValid(imageUri).not()) {
                    throw NotImageException()
                }
                localImageDatasource.getImageMultipartBodyPart(
                    uri = imageUri,
                    formDataName = "profileImage",
                    imageName = System.currentTimeMillis().toString(),
                )
            }
        runCatching {
            userDataSource.editUserProfile(
                image = imagePart,
            )
        }.onFailure {
            throw handleUserInfoEditException(it)
        }
    }

    override suspend fun editUserNickname(nickname: String) {
        runCatching {
            userDataSource.editUserNickname(request = UserNicknameRequest(nickname))
        }.onFailure {
            throw handleUserInfoEditException(it)
        }
    }

    override suspend fun getUserStatistics(userId: Long): UserStatistics {
        return userDataSource.getUserStatistics(userId).toUserStatistics()
    }

    private fun handleUserInfoEditException(throwable: Throwable) =
        if (throwable is HttpException) {
            val errorMessage = throwable.message()
            when (throwable.code()) {
                HTTP_BAD_REQUEST ->
                    UserInfoEditException.BadRequestException(errorMessage)

                HTTP_CONFLICT ->
                    UserInfoEditException.DuplicateNicknameException(errorMessage)

                else -> throwable
            }
        } else {
            throwable
        }
}
