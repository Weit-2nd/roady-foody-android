package com.weit2nd.domain.usecase.selectloction

import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.searchlocation.SearchLocationRepository
import javax.inject.Inject

class SearchLocationWithWordUseCase @Inject constructor(
    val repository: SearchLocationRepository
) {

    suspend operator fun invoke(searchWord: String): List<Location> {
        return repository.searchLocationsWithWord(searchWord)
    }
}
