package com.weit2nd.data.service

import com.weit2nd.data.model.search.FoodSpotsDTO
import com.weit2nd.data.model.search.PlaceWithCoordinateDTO
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

    @GET("/api/v1/address/coordinate")
    suspend fun getPlaceAddressWithCoordinate(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
    ): PlaceWithCoordinateDTO

    @GET("/api/v1/food-spots/search")
    suspend fun getFoodSpots(
        @Query("centerLongitude") centerLongitude: Double,
        @Query("centerLatitude") centerLatitude: Double,
        @Query("radius") radius: Int,
        @Query("name") name: String?,
        @Query("categoryIds") categoryIds: List<Long>,
    ): FoodSpotsDTO
}
