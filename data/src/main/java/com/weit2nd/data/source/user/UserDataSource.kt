package com.weit2nd.data.source.user

import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.service.UserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val service: UserService,
) {
    suspend fun getUserInfo(): UserDTO {
        return service.getUserInfo()
    }
}
