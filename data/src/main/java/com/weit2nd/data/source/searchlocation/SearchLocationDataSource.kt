package com.weit2nd.data.source.searchlocation

import com.weit2nd.data.model.LocationDTO
import com.weit2nd.domain.model.Coordinate
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchLocationDataSource @Inject constructor() {

    suspend fun getLocations(searchWord: String): List<LocationDTO> {
        return List(100) {
            LocationDTO(
                name = "위치이름$it",
                locationDetail = "서울 중구 세종대로",
                latitude = 0.0,
                longitude = 0.0
            )
        }
    }

    suspend fun getLocationWithCoordinate(coordinate: Coordinate): LocationDTO {
        delay(1000)
        return LocationDTO(
            name = "위치이름",
            locationDetail = "서울 중구 세종대로${coordinate.latitude}",
            latitude = 0.0,
            longitude = 0.0
        )
    }
}
