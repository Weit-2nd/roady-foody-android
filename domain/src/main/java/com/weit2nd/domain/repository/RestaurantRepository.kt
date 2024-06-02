package com.weit2nd.domain.repository

import com.weit2nd.domain.model.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
}
