package com.weit2nd.data.source.searchlocation

import com.weit2nd.data.model.LocationDTO
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
}
