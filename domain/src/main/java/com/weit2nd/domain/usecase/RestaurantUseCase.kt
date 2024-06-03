package com.weit2nd.domain.usecase

import com.weit2nd.domain.model.Restaurant
import com.weit2nd.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository,
) {
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants()
    }
}