package com.weit2nd.domain.usecase.selectloction

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.searchlocation.SearchLocationRepository
import javax.inject.Inject

class SearchLocationWithCoordinateUseCase @Inject constructor(
    val repository: SearchLocationRepository
) {

    suspend operator fun invoke(coordinate: Coordinate): Location {
        return repository.searchLocationWithCoordinate(coordinate)
    }
}
