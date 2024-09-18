package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.domain.model.spot.RatingCount
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingCountDTO(
    val rating: Int,
    val count: Int,
) : Parcelable

fun RatingCountDTO.toRatingCount() =
    RatingCount(
        rating = rating,
        count = count,
    )

fun RatingCount.toRatingCountDTO() =
    RatingCountDTO(
        rating = rating,
        count = count,
    )
