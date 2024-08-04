package com.weit2nd.domain.repository.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.TouristSpot

interface SearchPlaceRepository {
    suspend fun searchTouristSpot(
        count: Int,
        searchWord: String,
    ): List<TouristSpot>

    suspend fun searchPlacesWithWord(
        count: Int,
        searchWord: String,
    ): List<Place>

    suspend fun searchLocationWithCoordinate(coordinate: Coordinate): Place
}
