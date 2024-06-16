package com.weit2nd.presentation.ui.select.location.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.usecase.selectloction.SearchLocationWithCoordinateUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectLocationMapViewModel @Inject constructor(
    private val searchLocationWithCoordinateUseCase: SearchLocationWithCoordinateUseCase,
) : BaseViewModel<SelectLocationMapState, SelectLocationMapSideEffect>() {
    override val container =
        container<SelectLocationMapState, SelectLocationMapSideEffect>(SelectLocationMapState())

    fun onMapReady(map: KakaoMap) {
        SelectLocationMapIntent.StoreMap(map).post()
    }

    fun onCameraMoveEnd(coordinate: LatLng?) {
        SelectLocationMapIntent.SearchLocation(coordinate).post()
    }

    fun onClickCurrentPositionBtn(currentPosition: LatLng) {
        SelectLocationMapIntent.RequestCameraMove(currentPosition).post()
    }

    private fun SelectLocationMapIntent.post() = intent {
        when (this@post) {
            is SelectLocationMapIntent.StoreMap -> {
                reduce {
                    state.copy(
                        map = map
                    )
                }
            }

            is SelectLocationMapIntent.SearchLocation -> {
                if (coordinate != null) {
                    val location = searchLocationWithCoordinateUseCase.invoke(
                        Coordinate(
                            latitude = coordinate.latitude,
                            longitude = coordinate.longitude
                        )
                    )
                    reduce {
                        state.copy(
                            location = location
                        )
                    }
                }
            }

            is SelectLocationMapIntent.RequestCameraMove -> {
                container.stateFlow.value.map?.let { map ->
                    postSideEffect(SelectLocationMapSideEffect.MoveCamera(map, position))
                }
            }
        }
    }
}
