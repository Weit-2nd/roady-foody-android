package com.weit2nd.data.service

import com.weit2nd.data.model.search.PlacesDTO
import com.weit2nd.data.model.search.TouristSpotsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/api/v1/tourism/search")
    suspend fun getTourismSpots(
        @Query("numOfRows") numOfRows: Int,
        @Query("keyword") keyword: String,
    ): TouristSpotsDTO

    @GET("/api/v1/address/search")
    suspend fun getPlaceAddresses(
        @Query("numOfRows") numOfRows: Int,
        @Query("keyword") keyword: String,
    ): PlacesDTO
}
