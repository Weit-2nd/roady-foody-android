package com.weit2nd.data.source.search

import com.weit2nd.data.model.LocationDTO
import com.weit2nd.data.model.search.PlacesDTO
import com.weit2nd.data.model.search.TouristSpotsDTO
import com.weit2nd.data.service.SearchService
import com.weit2nd.domain.model.Coordinate
import kotlinx.coroutines.delay
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

    suspend fun getLocationsWithWord(
        count: Int,
        searchWord: String,
    ): PlacesDTO {
        return service.getPlaceAddresses(
            numOfRows = count,
            keyword = searchWord,
        )
    }

    suspend fun getLocationWithCoordinate(coordinate: Coordinate): LocationDTO {
        delay(1000)
        return LocationDTO(
            name = "위치이름",
            address = "서울 중구 세종대로${coordinate.latitude}",
            latitude = 0.0,
            longitude = 0.0,
        )
    }
}
