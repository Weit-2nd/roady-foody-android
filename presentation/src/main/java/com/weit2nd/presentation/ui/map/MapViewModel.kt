package com.weit2nd.presentation.ui.map

import com.kakao.vectormap.KakaoMap
import com.weit2nd.domain.usecase.GetRestaurantUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getRestaurantUseCase: GetRestaurantUseCase,
) : BaseViewModel<MapState, MapSideEffect>() {

    override val container = container<MapState, MapSideEffect>(MapState())

    fun onCameraMoveEnd(startLat: Double, startLng: Double, endLat: Double, endLng: Double) {
        MapIntent.RequestRestaurants(startLat, startLng, endLat, endLng).post()
    }

    fun onMapReady(kakaoMap: KakaoMap) {
        MapIntent.RefreshMarkers(kakaoMap).post()
    }

    private fun MapIntent.post() = intent {
        when (this@post) {
            is MapIntent.RequestRestaurants -> {
                runCatching {
                    val restaurants = getRestaurantUseCase.invoke(
                        startLat, startLng, endLat, endLng
                    ).map { it.toRestaurantState() }
                    reduce {
                        state.copy(
                            restaurants = restaurants
                        )
                    }
                }
            }

            is MapIntent.RefreshMarkers -> {
                reduce {
                    state.copy(
                        map = map
                    )
                }
                postSideEffect(
                    MapSideEffect.RefreshMarkers(
                        map,
                        container.stateFlow.value.restaurants
                    )
                )
            }
        }
    }

}
