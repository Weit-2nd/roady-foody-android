package com.weit2nd.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.HomeNavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<HomeState, HomeSideEffect>() {
    override val container =
        container<HomeState, HomeSideEffect>(
            HomeState(
                placeSearch = savedStateHandle[HomeNavRoutes.PLACE_SEARCH_KEY],
            ),
        )

    fun onClickReportBtn() {
        HomeIntent.NavToFoodSpotReport.post()
    }

    private fun HomeIntent.post() =
        intent {
            when (this@post) {
                HomeIntent.NavToFoodSpotReport -> {
                    postSideEffect(HomeSideEffect.NavToFoodSpotReport)
                }
            }
        }
}
