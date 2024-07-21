package com.weit2nd.data.source.search

import com.weit2nd.data.model.search.FoodSpotsDTO
import com.weit2nd.data.service.SearchService
import com.weit2nd.domain.model.Coordinate
import javax.inject.Inject

class SearchFoodSpotsDataSource @Inject constructor(
    private val searchService: SearchService,
) {
    suspend fun getFoodSpots(
        centerCoordinate: Coordinate,
        radius: Int,
        name: String?,
        categoryIds: List<Long>?,
    ): FoodSpotsDTO {
        return searchService
            .getFoodSpots(
                centerLongitude = centerCoordinate.longitude,
                centerLatitude = centerCoordinate.latitude,
                radius = radius,
                name = name,
                categoryIds = categoryIds,
            )
    }
}
