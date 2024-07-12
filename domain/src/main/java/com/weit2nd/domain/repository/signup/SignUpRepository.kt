package com.weit2nd.domain.repository.signup

import com.weit2nd.domain.model.NicknameState

interface SignUpRepository {
    suspend fun registerUser(
        image: String?,
        nickname: String,
        agreedTermIds: List<Long>,
    )

    fun verifyNickname(nickname: String): NicknameState

    suspend fun checkNicknameValidation(nickname: String): NicknameState
}
