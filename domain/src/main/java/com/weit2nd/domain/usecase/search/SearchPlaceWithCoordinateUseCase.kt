package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import javax.inject.Inject

class SearchPlaceWithCoordinateUseCase @Inject constructor(
    val repository: SearchPlaceRepository,
) {
    suspend operator fun invoke(coordinate: Coordinate): Place {
        return repository.searchPlaceWithCoordinate(coordinate)
    }
}
