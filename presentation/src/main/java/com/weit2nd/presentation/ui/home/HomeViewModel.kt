package com.weit2nd.presentation.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.usecase.search.SearchFoodSpotsUseCase
import com.weit2nd.domain.usecase.user.GetMyUserInfoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.HomeNavRoutes
import com.weit2nd.presentation.navigation.dto.CoordinateDTO
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMyUserInfoUseCase: GetMyUserInfoUseCase,
    private val searchFoodSpotsUseCase: SearchFoodSpotsUseCase,
) : BaseViewModel<HomeState, HomeSideEffect>() {
    private val placeSearch = savedStateHandle.get<PlaceSearchDTO>(HomeNavRoutes.PLACE_SEARCH_KEY)

    override val container =
        container<HomeState, HomeSideEffect>(
            HomeState(
                searchWords = placeSearch?.searchWords ?: "",
                initialLatLng =
                    placeSearch?.coordinate?.let {
                        LatLng.from(it.latitude, it.longitude)
                    } ?: LatLng.from(37.5597706, 126.9423666),
            ),
        )
    private var currentCameraPosition: LatLng = container.stateFlow.value.initialLatLng
    private var searchFoodSpotsJob: Job = Job().apply { complete() }

    fun onCreate() {
        HomeIntent.SetProfileImage.post()
    }

    fun onClickReportBtn() {
        HomeIntent.NavToFoodSpotReport.post()
    }

    fun onSearchPlaceClick() {
        HomeIntent.NavToSearch.post()
    }

    fun onProfileClick() {
        HomeIntent.NavToMyPage.post()
    }

    fun onMapReady(kakaoMap: KakaoMap) {
        HomeIntent.RefreshMarkers(kakaoMap).post()
    }

    fun onClickCurrentPositionBtn(currentPosition: LatLng) {
        HomeIntent.RequestCameraMove(currentPosition).post()
    }

    fun onMarkerClick(foodSpotId: Long) {
        HomeIntent.ShowFoodSpotSummary(foodSpotId).post()
    }

    fun onCameraMoveEnd(currentPosition: LatLng) {
        currentCameraPosition = currentPosition
        HomeIntent.ShowRetryButton.post()
    }

    fun onClickRefreshFoodSpotBtn(map: KakaoMap) {
        val viewport = map.viewport
        val x = viewport.width() / 2
        val y = viewport.height() / 2
        val centerPosition = map.fromScreenPoint(x, y)
        if (centerPosition != null) {
            searchFoodSpotsJob.cancel()
            searchFoodSpotsJob =
                HomeIntent
                    .RequestFoodSpots(
                        centerLat = centerPosition.latitude,
                        centerLng = centerPosition.longitude,
                    ).post()
        }
    }

    private fun HomeIntent.post() =
        intent {
            when (this@post) {
                HomeIntent.NavToFoodSpotReport -> {
                    postSideEffect(HomeSideEffect.NavToFoodSpotReport)
                }
                HomeIntent.NavToSearch -> {
                    postSideEffect(
                        HomeSideEffect.NavToSearch(
                            PlaceSearchDTO(
                                searchWords = state.searchWords,
                                coordinate =
                                    CoordinateDTO(
                                        currentCameraPosition.latitude,
                                        currentCameraPosition.longitude,
                                    ),
                            ),
                        ),
                    )
                }
                HomeIntent.NavToBack -> {
                    postSideEffect(HomeSideEffect.NavToBack)
                }

                is HomeIntent.NavToFoodSpotDetail -> {
                    postSideEffect(HomeSideEffect.NavToFoodSpotDetail(foodSpotId))
                }
                HomeIntent.NavToMyPage -> {
                    postSideEffect(HomeSideEffect.NavToMyPage)
                }

                is HomeIntent.RefreshMarkers -> {
                    reduce {
                        state.copy(
                            map = map,
                        )
                    }
                    postSideEffect(
                        HomeSideEffect.RefreshMarkers(
                            map,
                            state.foodSpots,
                        ),
                    )
                }
                is HomeIntent.RequestCameraMove -> {
                    state.map?.let { map ->
                        postSideEffect(HomeSideEffect.MoveCamera(map, position))
                    }
                }
                is HomeIntent.RequestFoodSpots -> {
                    reduce {
                        state.copy(
                            isMoved = false,
                        )
                    }
                    runCatching {
                        // TODO 이름, 카테고리 가져오기
                        // TODO radius를 넣을 때 유저 레벨?을 계산해서 넣기
                        val foodSpots =
                            searchFoodSpotsUseCase
                                .invoke(
                                    centerCoordinate =
                                        Coordinate(
                                            longitude = centerLng,
                                            latitude = centerLat,
                                        ),
                                    radius = 500,
                                    name = null,
                                    categoryIds = emptyList(),
                                ).map {
                                    it.toFoodSpotMarker()
                                }
                        reduce {
                            state.copy(
                                foodSpots = foodSpots,
                            )
                        }
                    }.onFailure { exception ->
                        Log.e("RequestFoodSpotsFail", "${exception.message}")
                    }
                }
                is HomeIntent.ShowFoodSpotSummary -> {
                    val updatedMarkers =
                        state.foodSpots
                            .toMutableList()
                            .apply {
                                replaceAll {
                                    val isSelected = it.id == foodSpotId
                                    it.copy(
                                        isSelected = isSelected,
                                    )
                                }
                            }.toList()
                    reduce {
                        state.copy(
                            foodSpots = updatedMarkers,
                        )
                    }
                }
                HomeIntent.ShowRetryButton -> {
                    reduce {
                        state.copy(
                            isMoved = true,
                        )
                    }
                }

                HomeIntent.SetProfileImage -> {
                    runCatching {
                        getMyUserInfoUseCase().profileImage.orEmpty()
                    }.onSuccess { image ->
                        reduce {
                            state.copy(
                                profileImage = image,
                            )
                        }
                    }.onFailure {
                        Log.d("MainTest", "$it")
                    }
                }
            }
        }
}
