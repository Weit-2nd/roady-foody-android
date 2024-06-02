package com.weit2nd.data.source

import com.weit2nd.data.model.RestaurantDTO
import kotlinx.coroutines.delay

class RestaurantDataSource {
    suspend fun getRestaurants(): List<RestaurantDTO> {
        delay(1000)
        return listOf(
            RestaurantDTO(0, 126.9423666, 37.5597706),
            RestaurantDTO(1, 126.9416726, 37.5140823),
            RestaurantDTO(2, 127.0247661, 37.503932),
            RestaurantDTO(3, 127.1001714, 37.5133497),
            RestaurantDTO(4, 127.0559819, 37.5445688),
        )
    }
}
