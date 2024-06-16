package com.weit2nd.data.repository.searchlocation

import com.weit2nd.data.model.LocationDTO
import com.weit2nd.data.source.searchlocation.SearchLocationDataSource
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.searchlocation.SearchLocationRepository
import javax.inject.Inject

class SearchLocationRepositoryImpl @Inject constructor(
    private val searchLocationDataSource: SearchLocationDataSource,
) : SearchLocationRepository {

    override suspend fun searchLocation(searchWord: String): List<Location> {
        return searchLocationDataSource.getLocations(searchWord).map { it.toLocation() }
    }

    override suspend fun searchLocationWithCoordinate(coordinate: Coordinate): Location {
        return searchLocationDataSource.getLocationWithCoordinate(coordinate).toLocation()
    }

    private fun LocationDTO.toLocation() = Location(
        name = name,
        locationDetail = locationDetail,
        coordinate = Coordinate(latitude = latitude, longitude = longitude),
    )
}
