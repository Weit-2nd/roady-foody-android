package com.weit2nd.domain.exception.user

sealed class LoginException : Throwable() {
    /**
     * 회원 가입이 안된 상태
     */
    class UserNotFoundException : LoginException()

    /**
     * 소셜 로그인 토큰이 만료된 상태
     *
     * 소셜 로그인을 다시 시도해주세요
     */
    class InvalidTokenException : LoginException()
}
