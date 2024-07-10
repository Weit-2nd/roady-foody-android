package com.weit2nd.data.source.signup

import com.weit2nd.data.model.login.LoginToken
import com.weit2nd.data.model.user.CheckNicknameDTO
import com.weit2nd.data.service.CheckNicknameService
import com.weit2nd.data.service.LoginService
import okhttp3.MultipartBody
import javax.inject.Inject

class SignUpDataSource @Inject constructor(
    private val loginService: LoginService,
    private val checkNicknameService: CheckNicknameService,
) {
    suspend fun signUp(
        image: MultipartBody.Part,
        signUpRequest: MultipartBody.Part,
    ): LoginToken {
        return loginService.signUp(
            signUp = signUpRequest,
            image = image,
        )
    }

    suspend fun checkNicknameDuplication(nickname: String): CheckNicknameDTO {
        return checkNicknameService.checkNickname(nickname)
    }
}
