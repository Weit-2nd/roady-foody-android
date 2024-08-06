package com.weit2nd.domain.repository.spot

import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.model.spot.ReportFoodSpotState

interface FoodSpotRepository {
    suspend fun reportFoodSpot(
        name: String,
        longitude: Double,
        latitude: Double,
        isFoodTruck: Boolean,
        open: Boolean,
        closed: Boolean,
        foodCategories: List<Long>,
        operationHours: List<OperationHour>,
        images: List<String>,
    )

    suspend fun verifyReport(
        name: String,
        longitude: Double?,
        latitude: Double?,
        foodCategories: List<Long>,
        images: List<String>,
    ): ReportFoodSpotState

    suspend fun deleteFoodSpotHistory(historyId: Long)

    suspend fun updateFoodSpotReport(
        foodSpotsId: Long,
        name: String?,
        longitude: Double?,
        latitude: Double?,
        open: Boolean?,
        closed: Boolean?,
        foodCategories: List<Long>?,
        operationHours: List<OperationHour>?,
    )
}
