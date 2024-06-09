package com.weit2nd.domain.usecase.signup

import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    val repository: SignUpRepository,
) {

    fun verifyNickname(nickname: String): NicknameState {
        return repository.verifyNickname(nickname)
    }

    suspend operator fun invoke(nickname: String): NicknameState {
        return repository.checkNicknameValidation(nickname)
    }
}
