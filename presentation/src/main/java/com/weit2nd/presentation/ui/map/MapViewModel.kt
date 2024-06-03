package com.weit2nd.presentation.ui.map

import com.weit2nd.domain.usecase.GetRestaurantUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.ui.home.HomeIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getRestaurantUseCase: GetRestaurantUseCase,
) : BaseViewModel<MapState, MapSideEffect>() {

    override val container = container<MapState, MapSideEffect>(MapState())

    init {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        HomeIntent.RequestRestaurants.post()
    }

    private fun HomeIntent.post() = intent {
        when (this@post) {
            HomeIntent.RequestRestaurants -> {
                runCatching {
                    val restaurants = getRestaurantUseCase.invoke().map { it.toRestaurantState() }
                    reduce {
                        state.copy(
                            restaurants = restaurants
                        )
                    }
                }
            }
        }
    }

}
