package com.weit2nd.data.source

import com.weit2nd.data.model.RestaurantDTO
import kotlinx.coroutines.delay

class RestaurantDataSource {
    suspend fun getRestaurants(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
    ): List<RestaurantDTO> {
        delay(1000)
        return listOf(
            RestaurantDTO(0, longitude = 126.9423666, latitude = 37.5597706),
            RestaurantDTO(1, longitude = 126.9416726, latitude = 37.5140823),
            RestaurantDTO(2, longitude = 127.0247661, latitude = 37.503932),
            RestaurantDTO(3, longitude = 127.1001714, latitude = 37.5133497),
            RestaurantDTO(4, longitude = 127.0559819, latitude = 37.5445688),
        )
    }
}
