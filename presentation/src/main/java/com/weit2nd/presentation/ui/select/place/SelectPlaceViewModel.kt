package com.weit2nd.presentation.ui.select.place

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.usecase.search.SearchPlacesWithWordUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectPlaceViewModel @Inject constructor(
    private val searchPlacesWithWordUseCase: SearchPlacesWithWordUseCase,
) : BaseViewModel<SelectPlaceState, SelectPlaceSideEffect>() {
    override val container =
        container<SelectPlaceState, SelectPlaceSideEffect>(SelectPlaceState())

    fun onValueChange(input: String) {
        SelectPlaceIntent.StoreSearchWord(input).post()
    }

    fun onLocationSearch() {
        SelectPlaceIntent.SearchPlace.post()
    }

    fun onClickPlace(place: Place) {
        SelectPlaceIntent
            .NavToMap(
                Coordinate(
                    latitude = place.latitude,
                    longitude = place.longitude,
                ),
            ).post()
    }

    fun onSelectPlace(place: Place) {
        SelectPlaceIntent.SelectPlace(place).post()
    }

    fun onClickBackBtn() {
        SelectPlaceIntent.NavToBack.post()
    }

    private fun SelectPlaceIntent.post() =
        intent {
            when (this@post) {
                is SelectPlaceIntent.StoreSearchWord -> {
                    reduce {
                        state.copy(
                            userInput = input,
                        )
                    }
                }

                SelectPlaceIntent.SearchPlace -> {
                    runCatching {
                        val result =
                            searchPlacesWithWordUseCase(searchWord = state.userInput)
                        reduce {
                            state.copy(
                                searchResults = result,
                            )
                        }
                    }
                }

                is SelectPlaceIntent.SelectPlace -> {
                    postSideEffect(SelectPlaceSideEffect.SelectPlace(place))
                }

                SelectPlaceIntent.NavToBack -> {
                    postSideEffect(SelectPlaceSideEffect.NavToBack)
                }

                is SelectPlaceIntent.NavToMap -> {
                    postSideEffect(SelectPlaceSideEffect.NavToMap(coordinate))
                }
            }
        }
}
