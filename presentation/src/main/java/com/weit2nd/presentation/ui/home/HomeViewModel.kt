package com.weit2nd.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import com.kakao.vectormap.LatLng
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.HomeNavRoutes
import com.weit2nd.presentation.navigation.dto.CoordinateDTO
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    fun onClickReportBtn() {
        HomeIntent.NavToFoodSpotReport.post()
    }

    fun onNavigateButtonClick() {
        HomeIntent.NavToBack.post()
    }

    fun onSearchPlaceClick() {
        HomeIntent.NavToSearch.post()
    }

    fun onCameraMoved(position: LatLng) {
        currentCameraPosition = position
    }

    fun onFoodSpotMarkerClick(foodSpotId: Long) {
        HomeIntent.NavToFoodSpotDetail(foodSpotId).post()
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
            }
        }
}
