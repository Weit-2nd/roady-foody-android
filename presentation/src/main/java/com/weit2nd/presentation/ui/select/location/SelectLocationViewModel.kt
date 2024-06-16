package com.weit2nd.presentation.ui.select.location

import com.weit2nd.domain.usecase.selectloction.SearchLocationUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val searchLocationUseCase: SearchLocationUseCase
) :
    BaseViewModel<SelectLocationState, SelectLocationSideEffect>() {
    override val container =
        container<SelectLocationState, SelectLocationSideEffect>(SelectLocationState())

    fun onLocationSearch(input: String) {
        SelectLocationIntent.SearchLocation(input).post()
    }

    private fun SelectLocationIntent.post() = intent {
        when (this@post) {
            is SelectLocationIntent.SearchLocation -> {
                runCatching {
                    val result = searchLocationUseCase.invoke(input)
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
