package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.BusinessState
import com.weit2nd.domain.model.search.FoodSpot
import com.weit2nd.presentation.util.getDistanceMeter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class FoodSpotMarker(
    val id: Long,
    val name: String,
    val image: String,
    val category: String,
    val position: LatLng,
    val distance: Int,
    val isFoodTruck: Boolean,
    val averageRating: Float,
    val reviewCount: Int,
    val businessState: BusinessState,
    val closeTime: LocalTime,
)

fun FoodSpot.toFoodSpotMarker(currentCoordinate: Coordinate): FoodSpotMarker =
    FoodSpotMarker(
        id = id,
        name = name,
        image = image,
        category = categories.firstOrNull().orEmpty(),
        position = LatLng.from(latitude, longitude),
        distance =
            getDistanceMeter(
                currentCoordinate,
                Coordinate(latitude, longitude),
            ),
        isFoodTruck = isFoodTruck,
        averageRating = averageRating,
        reviewCount = reviewCount,
        businessState = businessState,
        closeTime = operationHour.closingHours,
    )

private val operationHourFormat = DateTimeFormatter.ofPattern("HH:mm")
