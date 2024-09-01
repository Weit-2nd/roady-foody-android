package com.weit2nd.data.repository.search

import com.weit2nd.data.source.search.SearchHistoryDataSource
import com.weit2nd.domain.model.search.SearchHistory
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dataSource: SearchHistoryDataSource,
) : SearchHistoryRepository {
    override suspend fun getSearchHistories(): List<SearchHistory> {
        return dataSource.getSearchHistories()
    }

    override suspend fun addSearchHistory(searchHistory: SearchHistory) {
        val newHistories = dataSource.getSearchHistories().toMutableList()
        // 넣으려는 검색어가 첫번째에 있다면 동작이 불필요하기 때문에 패스
        val targetIndex = newHistories.indexOf(searchHistory).takeIf { it != 0 } ?: return
        if (targetIndex != -1) {
            newHistories.removeAt(targetIndex)
        }
        newHistories.add(0, searchHistory)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun removeSearchHistory(searchHistory: SearchHistory) {
        val newHistories = dataSource.getSearchHistories().minus(searchHistory)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun clearSearchHistory() {
        dataSource.setSearchHistory(emptyList())
    }
}
