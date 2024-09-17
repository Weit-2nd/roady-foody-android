package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.domain.model.spot.FoodCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodCategoryDTO(
    val id: Long,
    val name: String,
) : Parcelable

fun FoodCategoryDTO.toFoodCategory() =
    FoodCategory(
        id = id,
        name = name,
    )
