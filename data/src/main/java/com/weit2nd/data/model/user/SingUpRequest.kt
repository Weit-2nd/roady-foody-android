package com.weit2nd.data.model.user

data class SingUpRequest(
    val nickname: String,
    val agreedTermIds: List<Long>,
)
