package com.weit2nd.data.repository.user

import com.weit2nd.data.model.spot.FoodSpotCategoryDTO
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.spot.FoodSpotHistoryContentDTO
import com.weit2nd.data.model.spot.FoodSpotReportPhotoDTO
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.model.spot.FoodSpotPhoto
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
            categories = categories.map { it.toFoodSpotCategory() },
        )

    private fun FoodSpotReportPhotoDTO.toFoodSpotPhoto() =
        FoodSpotPhoto(
            id = id,
            url = url,
        )

    private fun FoodSpotCategoryDTO.toFoodSpotCategory() =
        FoodCategory(
            id = id,
            name = name,
        )
}
