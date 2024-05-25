package com.weit2nd.data.repository

import com.weit2nd.data.model.UserDTO
import com.weit2nd.data.source.LoginDataSource
import com.weit2nd.domain.model.User
import com.weit2nd.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource,
) : LoginRepository {
    override suspend fun login(): User {
        return loginDataSource.login().toUser()
    }

    private fun UserDTO.toUser() = User(
        name = name,
    )
}
