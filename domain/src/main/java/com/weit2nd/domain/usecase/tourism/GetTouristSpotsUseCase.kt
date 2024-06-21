package com.weit2nd.domain.usecase.tourism

import com.weit2nd.domain.model.tourism.TouristSpot
import com.weit2nd.domain.repository.tourism.TourismRepository
import javax.inject.Inject

class GetTouristSpotsUseCase @Inject constructor(
    private val repository: TourismRepository,
) {

    /**
     * 키워드로 관광지 정보를 가져옵니다.
     * @param count 가져올 개수 (default 10)
     * @param searchWord 검색 키워드
     */
    suspend operator fun invoke(
        count: Int = DEFAULT_COUNT,
        searchWord: String,
    ): List<TouristSpot> {
        return if (searchWord.isNotBlank()) {
            repository.searchTouristSpot(
                count = count,
                searchWord = searchWord,
            )
        } else {
            emptyList()
        }
    }

    companion object {
        private const val DEFAULT_COUNT = 10
    }
}
