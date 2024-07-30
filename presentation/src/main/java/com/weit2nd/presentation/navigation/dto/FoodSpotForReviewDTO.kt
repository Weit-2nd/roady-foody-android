package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodSpotForReviewDTO(
    val id: Long,
    val name: String,
) : Parcelable
