package com.weit2nd.domain.repository.searchlocation

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location

interface SearchLocationRepository {

    suspend fun searchLocationsWithWord(searchWord: String): List<Location>
    suspend fun searchLocationWithCoordinate(coordinate: Coordinate): Location
}
