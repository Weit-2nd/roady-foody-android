package com.weit2nd.domain.usecase.signup

import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class CheckNicknameDuplicateUseCase @Inject constructor(
    val repository: SignUpRepository,
) {
    suspend operator fun invoke(nickname: String): NicknameState {
        return repository.checkNicknameValidation(nickname)
    }
}
