package com.weit2nd.data.repository.search

import com.weit2nd.data.model.search.PlaceDTO
import com.weit2nd.data.model.search.PlaceWithCoordinateDTO
import com.weit2nd.data.model.search.TouristSpotDTO
import com.weit2nd.data.source.search.SearchPlaceDataSource
import com.weit2nd.domain.exception.SearchPlaceWithCoordinateException
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.TourismType
import com.weit2nd.domain.model.search.TouristSpot
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_INTERNAL_SERVER_ERROR
import retrofit2.HttpException
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
            .getPlacesWithWord(
                count = count,
                searchWord = searchWord,
            ).items
            .toPlaces()
    }

    override suspend fun searchPlaceWithCoordinate(coordinate: Coordinate): Place {
        return runCatching {
            dataSource.getPlaceWithCoordinate(coordinate).toPlace()
        }.getOrElse { throwable ->
            if (throwable is HttpException) {
                throw handleSearchPlaceWithCoordinateHttpException(throwable)
            } else {
                throw throwable
            }
        }
    }

    private fun PlaceWithCoordinateDTO.toPlace() =
        Place(
            placeName = "",
            addressName = addressName,
            roadAddressName = roadAddressName ?: "",
            longitude = longitude,
            latitude = latitude,
            tel = "",
        )

    private fun handleSearchPlaceWithCoordinateHttpException(throwable: HttpException): Throwable {
        return when (throwable.code()) {
            HTTP_INTERNAL_SERVER_ERROR ->
                SearchPlaceWithCoordinateException.ExternalApiException(
                    throwable.message(),
                )

            HTTP_BAD_REQUEST ->
                SearchPlaceWithCoordinateException.CanNotChangeToAddressException(
                    throwable.message(),
                )

            else -> throwable
        }
    }

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
