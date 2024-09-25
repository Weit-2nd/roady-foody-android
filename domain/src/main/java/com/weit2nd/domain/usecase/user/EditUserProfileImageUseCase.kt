package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class EditUserProfileImageUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend fun invoke(profileImage: String?) {
        repository.editUserInfo(profileImage = profileImage)
    }
}
