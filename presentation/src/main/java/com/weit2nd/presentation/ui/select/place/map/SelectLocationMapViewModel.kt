package com.weit2nd.presentation.ui.select.place.map

import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.usecase.search.SearchLocationWithCoordinateUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.SelectPlaceMapRoutes
import com.weit2nd.presentation.navigation.dto.CoordinateDTO
import com.weit2nd.presentation.navigation.dto.toCoordinate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectLocationMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchLocationWithCoordinateUseCase: SearchLocationWithCoordinateUseCase,
) : BaseViewModel<SelectLocationMapState, SelectLocationMapSideEffect>() {
    override val container =
        container<SelectLocationMapState, SelectLocationMapSideEffect>(
            SelectLocationMapState(
                initialPosition =
                    checkNotNull(
                        savedStateHandle
                            .get<CoordinateDTO>(
                                SelectPlaceMapRoutes.INITIAL_POSITION_KEY,
                            )?.toCoordinate(),
                    ),
            ),
        )
    private var searchLocationJob: Job = Job().apply { complete() }

    fun onMapReady(map: KakaoMap) {
        SelectLocationMapIntent.StoreMap(map).post()
    }

    fun onGloballyPositioned(offset: IntOffset) {
        SelectLocationMapIntent.StoreSelectMarkerOffset(offset).post()
    }

    fun onCameraMoveStart() {
        searchLocationJob.cancel()
        SelectLocationMapIntent.StartLocatingMap.post()
    }

    fun onCameraMoveEnd(coordinate: LatLng?) {
        SelectLocationMapIntent.SearchLocation(coordinate).post()
    }

    fun onClickCurrentPositionBtn(currentPosition: LatLng) {
        SelectLocationMapIntent.RequestCameraMove(currentPosition).post()
    }

    fun onClickSelectPlaceBtn() {
        SelectLocationMapIntent.SelectPlace(container.stateFlow.value.place).post()
    }

    private fun SelectLocationMapIntent.post() =
        intent {
            when (this@post) {
                is SelectLocationMapIntent.StoreMap -> {
                    reduce {
                        state.copy(
                            map = map,
                        )
                    }
                }

                is SelectLocationMapIntent.StoreSelectMarkerOffset -> {
                    reduce {
                        state.copy(
                            selectMarkerOffset = offset,
                        )
                    }
                }

                SelectLocationMapIntent.StartLocatingMap -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }
                }

                is SelectLocationMapIntent.SearchLocation -> {
                    if (coordinate == null) return@intent
                    searchLocationJob =
                        viewModelScope
                            .launch {
                                runCatching {
                                    val place =
                                        searchLocationWithCoordinateUseCase.invoke(
                                            Coordinate(
                                                latitude = coordinate.latitude,
                                                longitude = coordinate.longitude,
                                            ),
                                        )
                                    reduce {
                                        state.copy(
                                            place = place,
                                        )
                                    }
                                }.onFailure {
                                    postSideEffect(SelectLocationMapSideEffect.ShowToast("네트워크 오류가 발생했습니다."))
                                }
                            }.apply {
                                invokeOnCompletion {
                                    intent {
                                        reduce {
                                            state.copy(
                                                isLoading = false,
                                            )
                                        }
                                    }
                                }
                            }
                }

                is SelectLocationMapIntent.RequestCameraMove -> {
                    state.map?.let { map ->
                        postSideEffect(SelectLocationMapSideEffect.MoveCamera(map, position))
                    }
                }

                is SelectLocationMapIntent.SelectPlace -> {
                    postSideEffect(SelectLocationMapSideEffect.SelectPlace(place))
                }
            }
        }
}
