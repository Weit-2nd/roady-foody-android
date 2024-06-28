package com.weit2nd.data.source.signup

import com.weit2nd.data.model.user.CheckNicknameDTO
import com.weit2nd.data.service.CheckNicknameService
import com.weit2nd.data.service.SignUpService
import okhttp3.MultipartBody
import javax.inject.Inject

class SignUpDataSource @Inject constructor(
    private val signUpService: SignUpService,
    private val checkNicknameService: CheckNicknameService,
) {

    suspend fun signUp(
        image: MultipartBody.Part,
        signUpRequest: MultipartBody.Part,
    ) {
        signUpService.signUp(
            signUp = signUpRequest,
            image = image,
        )
    }

    suspend fun checkNicknameDuplication(
        nickname: String,
    ): CheckNicknameDTO {
        return checkNicknameService.checkNickname(nickname)
    }
}
