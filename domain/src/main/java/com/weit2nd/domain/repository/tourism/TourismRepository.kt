package com.weit2nd.domain.repository.tourism

import com.weit2nd.domain.model.tourism.TouristSpot

interface TourismRepository {
    suspend fun searchTouristSpot(
        count: Int,
        searchWord: String,
    ): List<TouristSpot>
}
