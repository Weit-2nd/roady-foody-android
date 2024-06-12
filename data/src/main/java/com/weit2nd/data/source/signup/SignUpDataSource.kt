package com.weit2nd.data.source.signup

import kotlinx.coroutines.delay

class SignUpDataSource {

    suspend fun checkNicknameValidation(nickname: String): Boolean {
        delay(1000)
        return false
    }
}
