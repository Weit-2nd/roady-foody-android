package com.weit2nd.data.source.search

import com.weit2nd.data.model.search.PlaceWithCoordinateDTO
import com.weit2nd.data.model.search.PlacesDTO
import com.weit2nd.data.model.search.TouristSpotsDTO
import com.weit2nd.data.service.SearchService
import com.weit2nd.domain.model.Coordinate
import javax.inject.Inject

class SearchPlaceDataSource @Inject constructor(
    private val service: SearchService,
) {
    suspend fun getTourismSpot(
        count: Int,
        searchWord: String,
    ): TouristSpotsDTO {
        return service.getTourismSpots(
            numOfRows = count,
            keyword = searchWord,
        )
    }

    suspend fun getPlacesWithWord(
        count: Int,
        searchWord: String,
    ): PlacesDTO {
        return service.getPlaceAddresses(
            numOfRows = count,
            keyword = searchWord,
        )
    }

    suspend fun getPlaceWithCoordinate(coordinate: Coordinate): PlaceWithCoordinateDTO {
        return service.getPlaceAddressWithCoordinate(
            latitude = coordinate.latitude,
            longitude = coordinate.longitude,
        )
    }
}
