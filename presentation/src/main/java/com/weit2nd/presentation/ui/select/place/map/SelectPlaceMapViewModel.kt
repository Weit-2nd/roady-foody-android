package com.weit2nd.presentation.ui.select.place.map

import android.util.Log
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.exception.SearchPlaceWithCoordinateException
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.usecase.search.SearchPlaceWithCoordinateUseCase
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
class SelectPlaceMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlaceWithCoordinateUseCase: SearchPlaceWithCoordinateUseCase,
) : BaseViewModel<SelectPlaceMapState, SelectPlaceMapSideEffect>() {
    override val container =
        container<SelectPlaceMapState, SelectPlaceMapSideEffect>(
            SelectPlaceMapState(
                initialPosition =
                    checkNotNull(
                        savedStateHandle
                            .get<CoordinateDTO>(
                                SelectPlaceMapRoutes.INITIAL_POSITION_KEY,
                            )?.toCoordinate(),
                    ),
            ),
        )
    private var searchPlaceJob: Job = Job().apply { complete() }

    fun onMapReady(map: KakaoMap) {
        SelectPlaceMapIntent.StoreMap(map).post()
    }

    fun onGloballyPositioned(offset: IntOffset) {
        SelectPlaceMapIntent.StoreSelectMarkerOffset(offset).post()
    }

    fun onCameraMoveStart() {
        searchPlaceJob.cancel()
        SelectPlaceMapIntent.StartPlaceMap.post()
    }

    fun onCameraMoveEnd(coordinate: LatLng?) {
        SelectPlaceMapIntent.SearchPlace(coordinate).post()
    }

    fun onClickCurrentPositionBtn(currentPosition: LatLng) {
        SelectPlaceMapIntent.RequestCameraMove(currentPosition).post()
    }

    fun onClickSelectPlaceBtn() {
        SelectPlaceMapIntent.SelectPlace(container.stateFlow.value.place).post()
    }

    private fun SelectPlaceMapIntent.post() =
        intent {
            when (this@post) {
                is SelectPlaceMapIntent.StoreMap -> {
                    reduce {
                        state.copy(
                            map = map,
                        )
                    }
                }

                is SelectPlaceMapIntent.StoreSelectMarkerOffset -> {
                    reduce {
                        state.copy(
                            selectMarkerOffset = offset,
                        )
                    }
                }

                SelectPlaceMapIntent.StartPlaceMap -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }
                }

                is SelectPlaceMapIntent.SearchPlace -> {
                    if (coordinate == null) return@intent
                    searchPlaceJob =
                        viewModelScope
                            .launch {
                                runCatching {
                                    val place =
                                        searchPlaceWithCoordinateUseCase.invoke(
                                            Coordinate(
                                                latitude = coordinate.latitude,
                                                longitude = coordinate.longitude,
                                            ),
                                        )
                                    reduce {
                                        state.copy(
                                            isAvailablePlace = true,
                                            place = place,
                                        )
                                    }
                                }.onFailure {
                                    if (it is SearchPlaceWithCoordinateException) {
                                        postSideEffect(SelectPlaceMapSideEffect.ShowToast(it.message.toString()))
                                    } else {
                                        Log.e(
                                            "SearchPlaceWithCoordinateError",
                                            it.message.toString(),
                                        )
                                    }
                                    reduce {
                                        state.copy(
                                            isAvailablePlace = false,
                                            place =
                                                Place(
                                                    placeName = "",
                                                    addressName = "",
                                                    roadAddressName = "",
                                                    longitude = 0.0,
                                                    latitude = 0.0,
                                                    tel = "",
                                                ),
                                        )
                                    }
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

                is SelectPlaceMapIntent.RequestCameraMove -> {
                    state.map?.let { map ->
                        postSideEffect(SelectPlaceMapSideEffect.MoveCamera(map, position))
                    }
                }

                is SelectPlaceMapIntent.SelectPlace -> {
                    postSideEffect(SelectPlaceMapSideEffect.SelectPlace(place))
                }
            }
        }
}
