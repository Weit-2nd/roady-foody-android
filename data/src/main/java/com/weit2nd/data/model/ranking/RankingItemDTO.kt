package com.weit2nd.data.model.ranking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingItemDTO(
    @field:Json(name = "ranking") val ranking: Int,
    @field:Json(name = "userNickname") val userNickname: String,
    @field:Json(name = "userId") val userId: Long,
    @field:Json(name = "profileImageUrl") val profileImageUrl: String,
    @field:Json(name = "rankChange") val rankChange: Int,
)
