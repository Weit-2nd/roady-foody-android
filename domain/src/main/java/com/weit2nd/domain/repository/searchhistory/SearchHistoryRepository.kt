package com.weit2nd.domain.repository.searchhistory

interface SearchHistoryRepository {

    suspend fun getSearchHistories(): List<String>
    suspend fun addSearchHistory(searchWord: String)
    suspend fun removeSearchHistory(searchWord: String)
    suspend fun clearSearchHistory()
}
