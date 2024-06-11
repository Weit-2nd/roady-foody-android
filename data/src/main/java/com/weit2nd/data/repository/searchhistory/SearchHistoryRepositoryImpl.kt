package com.weit2nd.data.repository.searchhistory

import com.weit2nd.data.source.SearchHistoryDataSource
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dataSource: SearchHistoryDataSource,
) : SearchHistoryRepository {
    override suspend fun getSearchHistories(): List<String> {
        return dataSource.getSearchHistories()
    }

    override suspend fun addSearchHistory(searchWord: String) {
        val newHistories = dataSource.getSearchHistories().toMutableList()
        if (newHistories.contains(searchWord)) {
            newHistories.remove(searchWord)
        }
        newHistories.add(0, searchWord)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun removeSearchHistory(searchWord: String) {
        val newHistories = dataSource.getSearchHistories().minus(searchWord)
        dataSource.setSearchHistory(newHistories)
    }

    override suspend fun clearSearchHistory() {
        dataSource.setSearchHistory(emptyList())
    }
}
