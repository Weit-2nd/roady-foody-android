package com.weit2nd.domain.repository.spot

interface FoodSpotRepository {
    suspend fun reportFoodSpot(
        name: String,
        longitude: Double,
        latitude: Double,
        isFoodTruck: Boolean,
        open: Boolean,
        closed: Boolean,
        images: List<String>,
    )

    suspend fun verifyReport(
        name: String,
        longitude: Double,
        latitude: Double,
        images: List<String>,
    )
}
