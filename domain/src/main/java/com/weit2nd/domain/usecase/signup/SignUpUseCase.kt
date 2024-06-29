package com.weit2nd.domain.usecase.signup

import com.weit2nd.domain.repository.signup.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignUpRepository,
) {

    /**
     * 회원가입을 합니다.
     * @param image 프로필 사진 uri
     * @param nickname 유저 닉네임
     * @param agreedTermIds 동의한 약관 리스트
     */
    suspend operator fun invoke(
        image: String,
        nickname: String,
        agreedTermIds: List<Long>,
    ) {
        repository.registerUser(
            image = image,
            nickname = nickname,
            agreedTermIds = agreedTermIds,
        )
    }
}
