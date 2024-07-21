package com.weit2nd.domain.repository.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.FoodSpot

interface SearchFoodSpotsRepository {
    suspend fun getFoodSpots(
        centerCoordinate: Coordinate,
        radius: Int,
        name: String?,
        categoryIds: List<Long>?,
    ): List<FoodSpot>
}
