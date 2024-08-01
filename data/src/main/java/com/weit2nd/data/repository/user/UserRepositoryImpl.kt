package com.weit2nd.data.repository.user

import com.weit2nd.data.model.user.UserDTO
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.domain.model.UserInfo
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
}
