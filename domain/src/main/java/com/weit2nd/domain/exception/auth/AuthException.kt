package com.weit2nd.domain.exception.auth

sealed class AuthException : Throwable() {
    class TokenNotFoundException : AuthException()
}
