package com.weit2nd.domain.model.spot

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

data class FoodSpotDetail(
    val id: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val movableFoodSpots: Boolean,
    val openState: FoodSpotOpenState,
    val storeClosure: Boolean,
    val operationHoursList: List<FoodSpotDetailOperationHours>,
    val foodCategoryList: List<FoodCategory>,
    val foodSpotsPhotos: List<FoodSpotPhoto>,
    val createdDateTime: LocalDateTime,
)

data class FoodSpotDetailOperationHours(
    val foodSpotsId: Long,
    val dayOfWeek: DayOfWeek,
    val openingHours: LocalTime,
    val closingHours: LocalTime,
)

enum class FoodSpotOpenState {
    OPEN,
    CLOSED,
    TEMPORARILY_CLOSED,
}
