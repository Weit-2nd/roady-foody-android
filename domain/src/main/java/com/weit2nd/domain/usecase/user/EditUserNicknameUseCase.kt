package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class EditUserNicknameUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend fun invoke(nickname: String) {
        repository.editUserNickname(nickname = nickname)
    }
}
