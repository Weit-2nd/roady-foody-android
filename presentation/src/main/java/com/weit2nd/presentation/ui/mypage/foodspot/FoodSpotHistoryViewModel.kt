package com.weit2nd.presentation.ui.mypage.foodspot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.exception.user.UserFoodSpotException
import com.weit2nd.domain.usecase.spot.GetFoodSpotHistoriesUseCase
import com.weit2nd.domain.usecase.user.GetUserStatisticsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.FoodSpotHistoryRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FoodSpotHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFoodSpotHistoriesUseCase: GetFoodSpotHistoriesUseCase,
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) : BaseViewModel<FoodSpotHistoryState, FoodSpotHistorySideEffect>() {
    override val container: Container<FoodSpotHistoryState, FoodSpotHistorySideEffect> =
        container(FoodSpotHistoryState())
    private val userId =
        savedStateHandle.get<Long>(FoodSpotHistoryRoutes.FOOD_SPOT_HISTORY_USER_ID_KEY) ?: 0L
    private var hasNext = AtomicBoolean(true)
    private var requestFoodSpotJob: Job = Job().apply { complete() }

    fun onCreate() {
        viewModelScope.launch {
            FoodSpotHistoryIntent.InitTotalCount.post()
        }
        FoodSpotHistoryIntent.LoadNextFoodSpots(null).post()
    }

    fun onNavigationClick() {
        requestFoodSpotJob = FoodSpotHistoryIntent.NavToBack.post()
    }

    fun onLastVisibleItemChanged(position: Int) {
        val currentFoodSpotSize = container.stateFlow.value.foodSpots.size
        val needNextPage = (currentFoodSpotSize - position) <= REMAINING_PAGE_FOR_REQUEST
        val isRequestEnable = requestFoodSpotJob.isCompleted && needNextPage && hasNext.get()
        if (isRequestEnable) {
            container.stateFlow.value.foodSpots.lastOrNull()?.let {
                requestFoodSpotJob = FoodSpotHistoryIntent.LoadNextFoodSpots(it.foodSpotsId).post()
            }
        }
    }

    fun onFoodSpotContentClick(foodSpotId: Long) {
        FoodSpotHistoryIntent.NavToFoodSpotDetail(foodSpotId).post()
    }

    private fun FoodSpotHistoryIntent.post() =
        intent {
            when (this@post) {
                FoodSpotHistoryIntent.NavToBack -> {
                    postSideEffect(FoodSpotHistorySideEffect.NavToBack)
                }

                is FoodSpotHistoryIntent.LoadNextFoodSpots -> {
                    runCatching {
                        getFoodSpotHistoriesUseCase.invoke(
                            userId = userId,
                            count = DEFAULT_LOAD_FOOD_SPOT_COUNT,
                            lastItemId = lastId,
                        )
                    }.onSuccess { reportedFoodSpot ->
                        reduce {
                            state.copy(
                                foodSpots = state.foodSpots.plus(reportedFoodSpot.contents),
                            )
                        }
                    }.onFailure {
                        if (it is UserFoodSpotException.NoMoreFoodSpotException) {
                            hasNext.set(false)
                        } else {
                            postSideEffect(FoodSpotHistorySideEffect.ShowNetworkErrorMessage)
                        }
                    }
                }

                is FoodSpotHistoryIntent.NavToFoodSpotDetail -> {
                    postSideEffect(FoodSpotHistorySideEffect.NavToFoodSpotDetail(foodSpotId))
                }

                FoodSpotHistoryIntent.InitTotalCount -> {
                    runCatching {
                        getUserStatisticsUseCase.invoke(userId).reportCount
                    }.onSuccess {
                        reduce {
                            state.copy(
                                totalCount = it,
                            )
                        }
                    }
                }
            }
        }

    companion object {
        private const val DEFAULT_LOAD_FOOD_SPOT_COUNT = 10
        private const val REMAINING_PAGE_FOR_REQUEST = 3
    }
}
