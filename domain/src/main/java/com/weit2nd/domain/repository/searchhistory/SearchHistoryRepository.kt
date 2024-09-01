package com.weit2nd.domain.repository.searchhistory

import com.weit2nd.domain.model.search.SearchHistory

interface SearchHistoryRepository {
    suspend fun getSearchHistories(): List<SearchHistory>

    suspend fun addSearchHistory(searchHistory: SearchHistory)

    suspend fun removeSearchHistory(searchHistory: SearchHistory)

    suspend fun clearSearchHistory()
}
