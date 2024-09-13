package com.weit2nd.data.model.review

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.data.model.spot.FoodSpotPhotoDTO
import com.weit2nd.data.model.spot.toFoodSpotPhoto
import com.weit2nd.data.util.StringToLocalDateTime
import com.weit2nd.domain.model.review.UserReview
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class UserReviewDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "contents") val contents: String,
    @field:Json(name = "rate") val rate: Int,
    @field:Json(name = "photos") val photos: List<FoodSpotPhotoDTO>,
    @StringToLocalDateTime @field:Json(name = "createdAt") val createdAt: LocalDateTime,
)

fun UserReviewDTO.toUserReview() =
    UserReview(
        id = id,
        contents = contents,
        rating = rate,
        photos = photos.map { it.toFoodSpotPhoto() },
        createdAt = createdAt,
    )
