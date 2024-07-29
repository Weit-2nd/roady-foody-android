package com.weit2nd.domain.usecase.login

import com.weit2nd.domain.exception.token.TokenState
import com.weit2nd.domain.repository.login.LoginRepository
import javax.inject.Inject

class GetTokenStateUseCase @Inject constructor(
    private val repository: LoginRepository,
) {
    /**
     * 토큰의 유효성을 판단한 후 그에 맞는 TokenState를 반환합니다.
     *
     * 스플래쉬 화면에서 자동 로그인을 시도할 때 사용
     */
    suspend operator fun invoke(): TokenState {
        return repository.getTokenState()
    }
}
