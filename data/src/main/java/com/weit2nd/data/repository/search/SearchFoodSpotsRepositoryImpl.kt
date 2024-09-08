package com.weit2nd.data.repository.search

import com.weit2nd.data.model.search.FoodSpotsDTO
import com.weit2nd.data.model.search.OperationHourDTO
import com.weit2nd.data.source.search.SearchFoodSpotsDataSource
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.FoodSpot
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.repository.search.SearchFoodSpotsRepository
import javax.inject.Inject

class SearchFoodSpotsRepositoryImpl @Inject constructor(
    private val searchFoodSpotsDataSource: SearchFoodSpotsDataSource,
) : SearchFoodSpotsRepository {
    override suspend fun getFoodSpots(
        centerCoordinate: Coordinate,
        radius: Int,
        name: String?,
        categoryIds: List<Long>,
    ): List<FoodSpot> {
        return searchFoodSpotsDataSource
            .getFoodSpots(
                centerCoordinate = centerCoordinate,
                radius = radius,
                name = name,
                categoryIds = categoryIds,
            ).toFoodSpots()
    }

    private fun FoodSpotsDTO.toFoodSpots(): List<FoodSpot> {
        return items.map { foodSpotDTO ->
            FoodSpot(
                id = foodSpotDTO.id,
                name = foodSpotDTO.name,
                longitude = foodSpotDTO.longitude,
                latitude = foodSpotDTO.latitude,
                businessState = foodSpotDTO.open,
                operationHour = foodSpotDTO.operationHour.toOperationHour(),
                categories = foodSpotDTO.categories,
                image = foodSpotDTO.image.orEmpty(),
                isFoodTruck = foodSpotDTO.isFoodTruck,
                averageRating = foodSpotDTO.averageRating,
                reviewCount = foodSpotDTO.reviewCount,
                createAt = foodSpotDTO.createAt,
            )
        }
    }

    private fun OperationHourDTO.toOperationHour() =
        OperationHour(
            dayOfWeek,
            openingHours,
            closingHours,
        )
}
