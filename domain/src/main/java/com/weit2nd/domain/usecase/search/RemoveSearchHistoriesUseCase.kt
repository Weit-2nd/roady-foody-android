package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.model.search.SearchHistory
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class RemoveSearchHistoriesUseCase @Inject constructor(
    private val repository: SearchHistoryRepository,
) {
    /**
     * 검색 기록을 제거 합니다.
     */
    suspend operator fun invoke(searchHistory: SearchHistory) {
        repository.removeSearchHistory(searchHistory)
    }
}
