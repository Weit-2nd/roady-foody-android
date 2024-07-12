package com.weit2nd.domain.usecase.logout

import com.weit2nd.domain.repository.logout.LogoutRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: LogoutRepository,
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
