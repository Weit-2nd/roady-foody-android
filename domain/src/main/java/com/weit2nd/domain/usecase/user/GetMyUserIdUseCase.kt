package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class GetMyUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Long {
        return userRepository.getMyUserId()
    }
}
