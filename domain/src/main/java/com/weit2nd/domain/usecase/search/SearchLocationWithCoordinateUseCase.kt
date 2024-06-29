package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import javax.inject.Inject

class SearchLocationWithCoordinateUseCase @Inject constructor(
    val repository: SearchPlaceRepository,
) {

    suspend operator fun invoke(coordinate: Coordinate): Location {
        return repository.searchLocationWithCoordinate(coordinate)
    }
}
