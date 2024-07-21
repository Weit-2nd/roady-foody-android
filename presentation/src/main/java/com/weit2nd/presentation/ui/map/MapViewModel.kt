package com.weit2nd.presentation.ui.map

import android.util.Log
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.usecase.search.SearchFoodSpotsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val searchFoodSpotsUseCase: SearchFoodSpotsUseCase,
) : BaseViewModel<MapState, MapSideEffect>() {
    override val container = container<MapState, MapSideEffect>(MapState())

    fun onCameraMoveEnd(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double,
    ) {
        MapIntent.RequestRestaurants(startLat, startLng, endLat, endLng).post()
    }

    fun onMapReady(kakaoMap: KakaoMap) {
        MapIntent.RefreshMarkers(kakaoMap).post()
    }

    fun onClickCurrentPositionBtn(currentPosition: LatLng) {
        MapIntent.RequestCameraMove(currentPosition).post()
    }

    private fun MapIntent.post() =
        intent {
            when (this@post) {
                is MapIntent.RequestRestaurants -> {
                    runCatching {
                        // TODO 중앙 좌표, 이름, 카테고리 가져오기
                        // TODO radius를 넣을 때 유저 레벨?을 계산해서 넣기
                        val foodSpots =
                            searchFoodSpotsUseCase
                                .invoke(
                                    centerCoordinate =
                                        Coordinate(
                                            longitude = 127.074667,
                                            latitude = 37.14703,
                                        ),
                                    radius = 500,
                                    name = null,
                                    categoryIds = null,
                                ).map {
                                    it.toFoodSpotState()
                                }
                        Log.d("MainTest", "$foodSpots")
                        reduce {
                            state.copy(
                                foodSpots = foodSpots,
                            )
                        }
                    }
                }

                is MapIntent.RefreshMarkers -> {
                    reduce {
                        state.copy(
                            map = map,
                        )
                    }
                    postSideEffect(
                        MapSideEffect.RefreshMarkers(
                            map,
                            state.foodSpots,
                        ),
                    )
                }

                is MapIntent.RequestCameraMove -> {
                    state.map?.let { map ->
                        postSideEffect(MapSideEffect.MoveCamera(map, position))
                    }
                }
            }
        }
}
