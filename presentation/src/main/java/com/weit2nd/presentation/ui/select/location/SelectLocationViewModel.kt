package com.weit2nd.presentation.ui.select.location

import com.weit2nd.domain.usecase.selectloction.SearchLocationWithWordUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val searchLocationWithWordUseCase: SearchLocationWithWordUseCase
) :
    BaseViewModel<SelectLocationState, SelectLocationSideEffect>() {
    override val container =
        container<SelectLocationState, SelectLocationSideEffect>(SelectLocationState())

    fun onValueChange(input: String) {
        SelectLocationIntent.StoreSearchWord(input).post()
    }

    fun onLocationSearch() {
        SelectLocationIntent.SearchLocation.post()
    }

    private fun SelectLocationIntent.post() = intent {
        when (this@post) {
            is SelectLocationIntent.StoreSearchWord -> {
                reduce {
                    state.copy(
                        userInput = input,
                    )
                }
            }

            SelectLocationIntent.SearchLocation -> {
                runCatching {
                    val result =
                        searchLocationWithWordUseCase.invoke(container.stateFlow.value.userInput)
                    reduce {
                        state.copy(
                            searchResults = result
                        )
                    }
                }
            }
        }
    }
}
