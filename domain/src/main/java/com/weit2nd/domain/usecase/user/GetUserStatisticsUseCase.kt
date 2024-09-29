package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.model.user.UserStatistics
import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserStatisticsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: Long): UserStatistics {
        return userRepository.getUserStatistics(userId)
    }
}
