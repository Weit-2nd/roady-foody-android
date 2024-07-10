package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import javax.inject.Inject

class SearchPlacesWithWordUseCase @Inject constructor(
    val repository: SearchPlaceRepository,
) {
    suspend operator fun invoke(
        count: Int = DEFAULT_COUNT,
        searchWord: String,
    ): List<Place> {
        return repository.searchPlacesWithWord(
            count = count,
            searchWord = searchWord,
        )
    }

    companion object {
        private const val DEFAULT_COUNT = 10
    }
}
