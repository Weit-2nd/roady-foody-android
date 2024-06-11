package com.weit2nd.data.repository.signup

import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: SignUpDataSource,
) : SignUpRepository {

    override fun verifyNickname(nickname: String): NicknameState {

        return when {
            nickname.isEmpty() -> NicknameState.EMPTY
            nickname.contains(' ') -> NicknameState.INVALID_CONTAIN_SPACE
            nickname.any { !it.isLetterOrDigit() && it !in '가'..'힣' } -> NicknameState.INVALID_CHARACTERS
            nickname.length < 6 || nickname.length > 16 -> NicknameState.INVALID_LENGTH
            else -> NicknameState.VALID
        }
    }

    override suspend fun checkNicknameValidation(nickname: String): NicknameState {
        val nicknameState = verifyNickname(nickname)
        if (nicknameState != NicknameState.VALID) return nicknameState

        return if (signUpDataSource.checkNicknameValidation(nickname)) {
            NicknameState.DUPLICATE
        } else {
            NicknameState.CAN_SIGN_UP
        }
    }
}
