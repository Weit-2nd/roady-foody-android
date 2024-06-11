package com.weit2nd.data.repository.signup

import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: SignUpDataSource,
) : SignUpRepository {

    override fun verifyNickname(nickname: String): NicknameState {
        val nicknameCondition = '가'..'힣'

        return when {
            nickname.isEmpty() -> NicknameState.EMPTY
            nickname.any { it.isWhitespace() } -> NicknameState.INVALID_CONTAIN_SPACE
            nickname.any {
                it.isLetterOrDigit().not() && it !in nicknameCondition
            } -> NicknameState.INVALID_CHARACTERS
            (nickname.length in 6..16).not() -> NicknameState.INVALID_LENGTH
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
