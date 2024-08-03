package com.weit2nd.data.source.user

import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.service.UserService
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
}
