package com.weit2nd.domain.usecase.signup

import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class VerifyNicknameUseCase @Inject constructor(
    val repository: SignUpRepository,
) {
    operator fun invoke(nickname: String): NicknameState {
        return repository.verifyNickname(nickname)
    }
}
