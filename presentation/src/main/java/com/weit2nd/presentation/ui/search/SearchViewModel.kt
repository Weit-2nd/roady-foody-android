package com.weit2nd.presentation.ui.search

import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.usecase.search.AddSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.ClearSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.GetSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.RemoveSearchHistoriesUseCase
import com.weit2nd.domain.usecase.search.SearchPlacesWithWordUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchHistoriesUseCase: GetSearchHistoriesUseCase,
    private val removeSearchHistoriesUseCase: RemoveSearchHistoriesUseCase,
    private val addSearchHistoriesUseCase: AddSearchHistoriesUseCase,
    private val clearSearchHistoriesUseCase: ClearSearchHistoriesUseCase,
    private val searchPlacesWithWordUseCase: SearchPlacesWithWordUseCase,
) : BaseViewModel<SearchState, SearchSideEffect>() {
    override val container: Container<SearchState, SearchSideEffect> =
        container(SearchState())

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
        SearchIntent.ChangeSearchWords("").post()
    }

    fun onSearchWordsChanged(searchWords: String) {
        SearchIntent.ChangeSearchWords(searchWords).post()
    }

    fun onSearchButtonClick() {
        SearchIntent.SearchWithWords(container.stateFlow.value.searchWords).post()
    }

    fun onHistoryClick(history: String) {
        SearchIntent.SearchWithWords(history).post()
    }

    fun onHistoryRemove(history: String) {
        SearchIntent.RemoveHistory(history).post()
    }

    fun onSearchResultClick(place: Place) {
        searchPlaceJob.cancel()
        searchPlaceJob = SearchIntent.SearchWithPlace(place).post()
    }

    private fun SearchIntent.post() =
        intent {
            when (this@post) {
                is SearchIntent.ChangeSearchWords -> {
                    reduce {
                        state.copy(
                            searchWords = searchWords,
                        )
                    }
                    runCatching {
                        if (searchWords.isNotBlank()) {
                            searchPlacesWithWordUseCase(searchWord = searchWords)
                        } else {
                            emptyList()
                        }
                    }.onSuccess { places ->
                        reduce {
                            state.copy(
                                searchResults = places,
                            )
                        }
                    }.onFailure {
                        postSideEffect(SearchSideEffect.ShowToastMessage(it.message.toString()))
                    }
                }
                is SearchIntent.RemoveHistory -> {
                    removeSearchHistoriesUseCase(history)
                    reduce {
                        state.copy(
                            histories = state.histories.minus(history),
                        )
                    }
                }
                is SearchIntent.SearchWithWords -> {
                    addSearchHistoriesUseCase(words)
                }
                is SearchIntent.SearchWithPlace -> {
                    addSearchHistoriesUseCase(place.placeName)
                }

                SearchIntent.GetSearchHistory -> {
                    val histories = getSearchHistoriesUseCase()
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
