package com.weit2nd.domain.model

data class User(
    val name: String,
)

data class UserInfo(
    val nickname: String,
    val profileImage: String?,
    val coin: Int,
)
