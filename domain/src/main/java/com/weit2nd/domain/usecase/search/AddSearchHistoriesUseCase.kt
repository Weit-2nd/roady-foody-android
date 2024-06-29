package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class AddSearchHistoriesUseCase @Inject constructor(
    private val repository: SearchHistoryRepository,
) {
    /**
     * 검색 기록을 추가 합니다.
     * 검색 기록은 리스트의 첫번째 index에 추가됩니다.
     *
     * 이미 존재하는 기록 이라면 해당 요소의 position을 첫번째로 옮깁니다.
     */
    suspend operator fun invoke(searchWord: String) {
        repository.addSearchHistory(searchWord)
    }
}
