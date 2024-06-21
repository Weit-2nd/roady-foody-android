package com.weit2nd.data.service

import com.weit2nd.data.model.tourism.TouristSpotsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface TourismService {
    @GET("/api/v1/tourism/search")
    suspend fun getTourismSpots(
        @Query("numOfRows") numOfRows: Int,
        @Query("keyword") keyword: String,
    ): TouristSpotsDTO
}
