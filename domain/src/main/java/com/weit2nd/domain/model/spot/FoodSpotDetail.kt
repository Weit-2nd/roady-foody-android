package com.weit2nd.domain.model.spot

import java.time.LocalDateTime

data class FoodSpotDetail(
    val id: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val movableFoodSpots: Boolean,
    val `open`: String,
    val storeClosure: Boolean,
    val operationHoursList: List<FoodSpotDetailOperationHours>,
    val foodCategoryList: List<FoodCategory>,
    val foodSpotsPhotos: List<FoodSpotPhoto>,
    val createdDateTime: LocalDateTime,
)

data class FoodSpotDetailOperationHours(
    val foodSpotsId: Long,
    val dayOfWeek: String,
    val openingHours: String,
    val closingHours: String,
)
