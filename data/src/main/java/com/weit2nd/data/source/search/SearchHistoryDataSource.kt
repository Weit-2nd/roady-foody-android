package com.weit2nd.data.source.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchHistoryDataSource @Inject constructor(
    private val context: Context,
) {

    private val Context.placeSearchHistoryDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "searchHistory"
    )

    suspend fun getSearchHistories(): List<String> {
        return context.placeSearchHistoryDataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preference ->
                preference[KEY_SEARCH_HISTORY] ?: emptyList()
            }.first().toList()
    }

    suspend fun setSearchHistory(searchWords: List<String>) {
        context.placeSearchHistoryDataStore.edit { preference ->
            preference[KEY_SEARCH_HISTORY] = searchWords.toSet()
        }
    }

    private companion object{
        val KEY_SEARCH_HISTORY = stringSetPreferencesKey(name = "search_history")
    }
}
