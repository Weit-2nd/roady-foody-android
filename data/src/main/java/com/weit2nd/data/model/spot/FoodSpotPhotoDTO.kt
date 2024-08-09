package com.weit2nd.data.model.spot

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.weit2nd.domain.model.spot.FoodSpotPhoto

@JsonClass(generateAdapter = true)
data class FoodSpotPhotoDTO(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "url") val image: String,
)

fun FoodSpotPhotoDTO.toFoodSpotPhoto() =
    FoodSpotPhoto(
        id = id,
        image = image,
    )
