package com.weit2nd.domain.usecase.searchhistory

import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class ClearSearchHistoriesUseCase @Inject constructor(
    private val repository: SearchHistoryRepository,
) {
    /**
     * 모든 검색 기록을 정리합니다.
     */
    suspend operator fun invoke() {
        repository.clearSearchHistory()
    }
}
