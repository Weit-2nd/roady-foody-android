package com.weit2nd.domain.model

data class RankingItem(
    val ranking: Int,
    val userNickname: String,
    val userId: Long,
    val profileImageUrl: String,
    val rankChange: Int,
)
