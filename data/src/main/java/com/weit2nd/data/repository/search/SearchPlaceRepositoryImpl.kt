package com.weit2nd.data.repository.search

import com.weit2nd.data.model.LocationDTO
import com.weit2nd.data.model.search.PlaceDTO
import com.weit2nd.data.model.search.TouristSpotDTO
import com.weit2nd.data.source.search.SearchPlaceDataSource
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.TourismType
import com.weit2nd.domain.model.search.TouristSpot
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import javax.inject.Inject

class SearchPlaceRepositoryImpl @Inject constructor(
    private val dataSource: SearchPlaceDataSource,
) : SearchPlaceRepository {
    override suspend fun searchTouristSpot(
        count: Int,
        searchWord: String,
    ): List<TouristSpot> {
        return dataSource
            .getTourismSpot(
                count = count,
                searchWord = searchWord,
            ).items
            .toTouristSpots()
    }

    override suspend fun searchPlacesWithWord(
        count: Int,
        searchWord: String,
    ): List<Place> {
        return dataSource
            .getLocationsWithWord(
                count = count,
                searchWord = searchWord,
            ).items
            .toPlaces()
    }

    override suspend fun searchLocationWithCoordinate(coordinate: Coordinate): Location {
        return dataSource.getLocationWithCoordinate(coordinate).toLocation()
    }

    private fun LocationDTO.toLocation() =
        Location(
            name = name,
            address = address,
            coordinate = Coordinate(latitude = latitude, longitude = longitude),
        )

    private fun List<PlaceDTO>.toPlaces(): List<Place> {
        return this.map { it.toPlace() }
    }

    private fun PlaceDTO.toPlace(): Place {
        return Place(
            placeName = placeName,
            addressName = addressName,
            roadAddressName = roadAddressName,
            longitude = longitude,
            latitude = latitude,
            tel = tel,
        )
    }

    private fun List<TouristSpotDTO>.toTouristSpots(): List<TouristSpot> {
        return this.map { it.toTouristSpot() }
    }

    private fun TouristSpotDTO.toTouristSpot(): TouristSpot {
        return TouristSpot(
            title = title,
            mainAddress = mainAddress,
            secondaryAddress = secondaryAddress,
            longitude = longitude,
            latitude = latitude,
            tel = tel,
            thumbnailImage = thumbnailImage,
            tourismType = TourismType.valueOf(tourismType),
        )
    }
}
