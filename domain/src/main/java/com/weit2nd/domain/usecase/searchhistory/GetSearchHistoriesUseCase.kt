package com.weit2nd.domain.usecase.searchhistory

import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class GetSearchHistoriesUseCase @Inject constructor(
    private val repository: SearchHistoryRepository,
) {
    /**
     * 최근에 추가된 기록 순서로 정렬된 검색 기록 리스트를 가져옵니다.
     */
    suspend operator fun invoke(): List<String> {
        return repository.getSearchHistories()
    }
}
