package com.weit2nd.data.source.user

import com.weit2nd.data.model.review.UserReviewsDTO
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.model.user.UserNicknameRequest
import com.weit2nd.data.service.UserService
import okhttp3.MultipartBody
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val service: UserService,
) {
    suspend fun getMyUserInfo(): UserDTO {
        return service.getMyUserInfo()
    }

    suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): FoodSpotHistoriesDTO {
        return service.getFoodSpotHistories(
            userId = userId,
            size = count,
            lastId = lastItemId,
        )
    }

    suspend fun getUserReviews(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): UserReviewsDTO {
        return service.getUserReviews(
            userId = userId,
            size = count,
            lastId = lastItemId,
        )
    }

    suspend fun editUserProfile(image: MultipartBody.Part?) {
        service.editUserProfile(image)
    }

    suspend fun editUserNickname(request: UserNicknameRequest) {
        service.editUserNickname(request)
    }
}
