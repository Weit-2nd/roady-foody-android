package com.weit2nd.data.source.toursim

import com.weit2nd.data.model.tourism.TouristSpotsDTO
import com.weit2nd.data.service.TourismService
import javax.inject.Inject

class TourismDataSource @Inject constructor(
    private val service: TourismService,
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
}
