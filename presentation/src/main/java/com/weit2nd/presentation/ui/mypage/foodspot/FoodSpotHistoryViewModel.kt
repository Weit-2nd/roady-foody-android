package com.weit2nd.presentation.ui.mypage.foodspot

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.exception.user.UserFoodSpotException
import com.weit2nd.domain.usecase.spot.GetFoodSpotHistoriesUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.FoodSpotHistoryRoutes
import com.weit2nd.presentation.ui.mypage.review.ReviewHistorySideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FoodSpotHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFoodSpotHistoriesUseCase: GetFoodSpotHistoriesUseCase,
) : BaseViewModel<FoodSpotHistoryState, FoodSpotHistorySideEffect>() {
    override val container: Container<FoodSpotHistoryState, FoodSpotHistorySideEffect> =
        container(FoodSpotHistoryState())
    private val userId =
        savedStateHandle.get<Long>(FoodSpotHistoryRoutes.FOOD_SPOT_HISTORY_USER_ID_KEY) ?: 0L
    private var hasNext = AtomicBoolean(true)
    private var requestFoodSpotJob: Job = Job().apply { complete() }

    fun onCreate() {
        FoodSpotHistoryIntent.LoadNextFoodSpots(null).post()
    }

    fun onNavigationClick() {
        requestFoodSpotJob = FoodSpotHistoryIntent.NavToBack.post()
    }

    fun onLastVisibleItemChanged(position: Int) {
        val currentReviewSize = container.stateFlow.value.foodSpots.size
        val needNextPage = (currentReviewSize - position) <= REMAINING_PAGE_FOR_REQUEST
        val isRequestEnable = requestFoodSpotJob.isCompleted && needNextPage && hasNext.get()
        if (isRequestEnable) {
            container.stateFlow.value.foodSpots.lastOrNull()?.let {
                requestFoodSpotJob = FoodSpotHistoryIntent.LoadNextFoodSpots(it.foodSpotsId).post()
            }
        }
    }

    private fun FoodSpotHistoryIntent.post() =
        intent {
            when (this@post) {
                FoodSpotHistoryIntent.NavToBack -> {
                    ReviewHistorySideEffect.NavToBack
                }

                is FoodSpotHistoryIntent.LoadNextFoodSpots -> {
                    // TODO 응답으로 총 개수도 받아와야함
                    runCatching {
                        getFoodSpotHistoriesUseCase.invoke(
                            userId = userId,
                            count = DEFAULT_LOAD_FOOD_SPOT_COUNT,
                            lastItemId = lastId,
                        )
                    }.onSuccess { reportedFoodSpot ->
                        reduce {
                            state.copy(
                                foodSpots = reportedFoodSpot.contents,
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
            }
        }

    companion object {
        private const val DEFAULT_LOAD_FOOD_SPOT_COUNT = 10
        private const val REMAINING_PAGE_FOR_REQUEST = 3
    }
}
