package com.weit2nd.presentation.ui.home

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.BusinessState
import com.weit2nd.domain.model.search.FoodSpot
import com.weit2nd.presentation.util.getDistanceMeter
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class FoodSpotMarker(
    val id: Long,
    val name: String,
    val image: String = "",
    val category: String = "붕어빵",
    val position: LatLng,
    val distance: Int,
    val isFoodTruck: Boolean = true,
    val averageRate: Float = 4.8f,
    val reviewCount: Int = Random(System.currentTimeMillis()).nextInt(150),
    val businessState: BusinessState,
    val closeTime: String = "22:00",
)

fun FoodSpot.toFoodSpotMarker(currentCoordinate: Coordinate): FoodSpotMarker =
    FoodSpotMarker(
        id = id,
        name = name,
        position = LatLng.from(latitude, longitude),
        distance =
            getDistanceMeter(
                currentCoordinate,
                Coordinate(latitude, longitude),
            ),
        businessState = businessState,
    )

private val operationHourFormat = DateTimeFormatter.ofPattern("HH:mm")
