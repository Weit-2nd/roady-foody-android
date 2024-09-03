package com.weit2nd.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.SearchHistory
import com.weit2nd.domain.usecase.search.AddSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.ClearSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.GetSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.RemoveSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.SearchPlacesWithWordUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.toSearchPlaceResult
import com.weit2nd.presentation.navigation.SearchRoutes
import com.weit2nd.presentation.navigation.dto.CoordinateDTO
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.navigation.dto.toCoordinate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSearchHistoriesUseCase: GetSearchHistoriesUseCase,
    private val removeSearchHistoriesUseCase: RemoveSearchHistoriesUseCase,
    private val addSearchHistoriesUseCase: AddSearchHistoriesUseCase,
    private val clearSearchHistoriesUseCase: ClearSearchHistoriesUseCase,
    private val searchPlacesWithWordUseCase: SearchPlacesWithWordUseCase,
) : BaseViewModel<SearchState, SearchSideEffect>() {
    private val placeSearch =
        savedStateHandle.get<PlaceSearchDTO>(SearchRoutes.INITIAL_SEARCH_WORDS_KEY) ?: PlaceSearchDTO(
            "",
            CoordinateDTO(0.0, 0.0),
        )

    private val historyCache = CopyOnWriteArrayList<SearchHistory>()

    override val container: Container<SearchState, SearchSideEffect> =
        container(
            SearchState(
                searchWords = placeSearch.searchWords,
            ),
        )

    private var searchPlaceJob: Job =
        Job().apply {
            complete()
        }

    fun onCreate() {
        SearchIntent.GetSearchHistory.post()
    }

    fun onNavigationButtonClick() {
        SearchIntent.NavToBack.post()
    }

    fun onSearchWordsClear() {
        searchPlaceJob.cancel()
        searchPlaceJob = SearchIntent.ChangeSearchWords("").post()
    }

    fun onSearchWordsChanged(searchWords: String) {
        searchPlaceJob.cancel()
        searchPlaceJob = SearchIntent.ChangeSearchWords(searchWords).post()
    }

    fun onSearchButtonClick() {
        SearchIntent.SearchWithWords(container.stateFlow.value.searchWords).post()
    }

    fun onHistoryClick(history: SearchHistory) {
        if (history.isPlace) {
            SearchIntent
                .SearchWithPlace(
                    name = history.words,
                    coordinate = history.coordinate,
                ).post()
        } else {
            SearchIntent.SearchWithWords(history.words).post()
        }
    }

    fun onHistoryRemove(history: SearchHistory) {
        SearchIntent.RemoveHistory(history).post()
    }

    fun onSearchResultClick(place: Place) {
        SearchIntent
            .SearchWithPlace(
                name = place.placeName,
                coordinate =
                    Coordinate(
                        latitude = place.latitude,
                        longitude = place.longitude,
                    ),
            ).post()
    }

    private fun SearchIntent.post() =
        intent {
            when (this@post) {
                is SearchIntent.ChangeSearchWords -> {
                    val histories =
                        if (searchWords.isNotBlank()) {
                            historyCache.filter { it.words.contains(searchWords) }
                        } else {
                            historyCache
                        }
                    reduce {
                        state.copy(
                            searchWords = searchWords,
                            histories = histories,
                        )
                    }
                    runCatching {
                        if (searchWords.isNotBlank()) {
                            searchPlacesWithWordUseCase(searchWord = searchWords)
                        } else {
                            emptyList()
                        }
                    }.onSuccess { places ->
                        val coordinate = placeSearch.coordinate.toCoordinate()
                        val searchResults =
                            places.map {
                                it.toSearchPlaceResult(coordinate)
                            }
                        reduce {
                            state.copy(
                                searchResults = searchResults,
                            )
                        }
                    }.onFailure {
                        if (it !is CancellationException) {
                            postSideEffect(SearchSideEffect.ShowToastMessage(it.message.toString()))
                        }
                    }
                }
                is SearchIntent.RemoveHistory -> {
                    removeSearchHistoriesUseCase(history)
                    historyCache.remove(history)
                    reduce {
                        state.copy(
                            histories = state.histories.minus(history),
                        )
                    }
                }
                is SearchIntent.SearchWithWords -> {
                    val searchHistory =
                        SearchHistory(
                            words = words,
                            coordinate =
                                Coordinate(
                                    latitude = placeSearch.coordinate.latitude,
                                    longitude = placeSearch.coordinate.longitude,
                                ),
                            isPlace = false,
                        )
                    addSearchHistoriesUseCase(searchHistory)
                    val placeSearch =
                        PlaceSearchDTO(
                            searchWords = words,
                            coordinate = placeSearch.coordinate,
                        )
                    postSideEffect(SearchSideEffect.NavToHome(placeSearch))
                }
                is SearchIntent.SearchWithPlace -> {
                    val searchHistory =
                        SearchHistory(
                            words = name,
                            coordinate = coordinate,
                            isPlace = true,
                        )
                    addSearchHistoriesUseCase(searchHistory)
                    val coordinate =
                        CoordinateDTO(
                            latitude = coordinate.latitude,
                            longitude = coordinate.longitude,
                        )
                    val placeSearch =
                        PlaceSearchDTO(
                            searchWords = name,
                            coordinate = coordinate,
                        )
                    postSideEffect(SearchSideEffect.NavToHome(placeSearch))
                }

                SearchIntent.GetSearchHistory -> {
                    val histories = getSearchHistoriesUseCase()
                    historyCache.addAll(histories)
                    reduce {
                        state.copy(
                            histories = histories,
                        )
                    }
                }

                SearchIntent.NavToBack -> {
                    postSideEffect(SearchSideEffect.NavToBack)
                }
            }
        }
}
