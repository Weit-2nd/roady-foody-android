package com.weit2nd.domain.usecase

import com.weit2nd.domain.model.Restaurant
import com.weit2nd.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository,
) {
    suspend operator fun invoke(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
    ): List<Restaurant> {
        return repository.getRestaurants(startLat, startLng, endLat, endLng)
    }
}
