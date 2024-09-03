package com.weit2nd.data.source.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.weit2nd.data.SearchHistoriesPreferences
import com.weit2nd.data.SearchHistoryPreferences
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.SearchHistory
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchHistoryDataSource @Inject constructor(
    private val context: Context,
) {
    private val Context.dataStore: DataStore<SearchHistoriesPreferences> by dataStore(
        fileName = SEARCH_HISTORIES_FILE_NAME,
        serializer = SearchHistoriesSerializer,
    )

    suspend fun getSearchHistories(): List<SearchHistory> {
        return context.dataStore.data
            .first()
            .toSearchHistories()
    }

    suspend fun setSearchHistory(searchHistories: List<SearchHistory>) {
        context.dataStore.updateData { preferences ->
            val histories =
                searchHistories.map {
                    it.toSearchHistoryPreferences()
                }
            preferences
                .toBuilder()
                .clear()
                .addAllHistories(histories)
                .build()
        }
    }

    private fun SearchHistoryPreferences.toSearchHistory() =
        SearchHistory(
            words = words,
            coordinate = Coordinate(latitude, longitude),
            isPlace = isPlace,
        )

    private fun SearchHistoriesPreferences.toSearchHistories() =
        buildList {
            repeat(this@toSearchHistories.historiesCount) { position ->
                add(getHistories(position).toSearchHistory())
            }
        }

    private fun SearchHistory.toSearchHistoryPreferences() =
        SearchHistoryPreferences
            .newBuilder()
            .setWords(words)
            .setLatitude(coordinate.latitude)
            .setLongitude(coordinate.longitude)
            .setIsPlace(isPlace)
            .build()

    private companion object {
        private const val SEARCH_HISTORIES_FILE_NAME = "search_histories.pb"
    }
}
