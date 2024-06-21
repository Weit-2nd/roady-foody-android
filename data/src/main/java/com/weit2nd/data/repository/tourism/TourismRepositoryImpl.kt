package com.weit2nd.data.repository.tourism

import com.weit2nd.data.model.tourism.TouristSpotDTO
import com.weit2nd.data.source.toursim.TourismDataSource
import com.weit2nd.domain.model.tourism.TourismType
import com.weit2nd.domain.model.tourism.TouristSpot
import com.weit2nd.domain.repository.tourism.TourismRepository
import javax.inject.Inject

class TourismRepositoryImpl @Inject constructor(
    private val dataSource: TourismDataSource,
) : TourismRepository {
    override suspend fun searchTouristSpot(
        count: Int,
        searchWord: String,
    ): List<TouristSpot> {
        return dataSource.getTourismSpot(
            count = count,
            searchWord = searchWord,
        ).items.toTouristSpots()
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
