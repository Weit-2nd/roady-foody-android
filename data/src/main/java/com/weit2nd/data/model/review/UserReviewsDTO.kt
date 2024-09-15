package com.weit2nd.data.model.review

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReviewsDTO(
    @field:Json(name = "contents") val contents: List<UserReviewDTO>,
    @field:Json(name = "hasNext") val hasNext: Boolean,
)

fun UserReviewsDTO.toUserReviews() =
    contents.map {
        it.toUserReview()
    }
