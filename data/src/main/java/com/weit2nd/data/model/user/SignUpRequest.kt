package com.weit2nd.data.model.user

data class SignUpRequest(
    val nickname: String,
    val agreedTermIds: List<Long>,
)
