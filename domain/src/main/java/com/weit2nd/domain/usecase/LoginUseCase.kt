package com.weit2nd.domain.usecase

import com.weit2nd.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.loginWithKakao()
    }
}
