package com.weit2nd.data.repository

import com.weit2nd.data.model.RestaurantDTO
import com.weit2nd.data.source.RestaurantDataSource
import com.weit2nd.domain.model.Restaurant
import com.weit2nd.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDataSource: RestaurantDataSource,
) : RestaurantRepository {
    override suspend fun getRestaurants(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): List<Restaurant> {
        return restaurantDataSource.getRestaurants(
            startLat, startLng, endLat, endLng
        ).map { it.toRestaurant() }
    }

    private fun RestaurantDTO.toRestaurant() = Restaurant(
        id = id,
        latitude = latitude,
        longitude = longitude,
    )
}
