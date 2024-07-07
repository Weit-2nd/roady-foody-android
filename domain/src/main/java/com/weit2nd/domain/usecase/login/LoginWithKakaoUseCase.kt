package com.weit2nd.domain.usecase.login

import com.weit2nd.domain.repository.login.LoginRepository
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val repository: LoginRepository,
) {

    /**
     * 카카오로 로그인 합니다.
     *
     * 로그인 화면 또는 스플래쉬 화면에서 소셜 토큰이 만료됐을 때 사용
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.loginWithKakao()
    }
}
