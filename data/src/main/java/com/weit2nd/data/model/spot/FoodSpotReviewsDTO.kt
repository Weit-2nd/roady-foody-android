package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.util.StringToLocalDateTime
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class FoodSpotReviewsDTO(
    @field:Json(name = "contents") val contents: List<FoodSpotReviewContentDTO>,
    @field:Json(name = "hasNext") val hasNext: Boolean,
)

@JsonClass(generateAdapter = true)
data class FoodSpotReviewContentDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "foodSpotsId") val foodSpotsId: Long,
    @field:Json(name = "userInfo") val userInfo: FoodSpotReviewUserInfoDTO,
    @field:Json(name = "contents") val contents: String,
    @field:Json(name = "rate") val rate: Int,
    @field:Json(name = "photos") val photos: List<FoodSpotReviewPhotoDTO>,
    @StringToLocalDateTime @Json(name = "createdAt") val createdAt: LocalDateTime,
)

@JsonClass(generateAdapter = true)
data class FoodSpotReviewUserInfoDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "nickname") val nickname: String,
    @field:Json(name = "url") val profileImage: String?,
)

@JsonClass(generateAdapter = true)
data class FoodSpotReviewPhotoDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "url") val image: String,
)
