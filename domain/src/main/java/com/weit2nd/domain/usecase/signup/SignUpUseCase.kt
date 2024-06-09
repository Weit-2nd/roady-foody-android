package com.weit2nd.domain.usecase.signup

import javax.inject.Inject

class SignUpUseCase @Inject constructor() {

    fun verifyNickname(nickname: String): Boolean {
        val regex = "^[가-힣a-zA-Z0-9]{6,16}$".toRegex()
        return nickname.matches(regex)
    }
}
