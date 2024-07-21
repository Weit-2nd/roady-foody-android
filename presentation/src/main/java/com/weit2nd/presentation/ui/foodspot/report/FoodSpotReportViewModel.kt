package com.weit2nd.presentation.ui.foodspot.report

import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FoodSpotReportViewModel @Inject constructor() : BaseViewModel<FoodSpotReportState, FoodSpotReportSideEffect>() {
    override val container =
        container<FoodSpotReportState, FoodSpotReportSideEffect>(FoodSpotReportState())

    fun onSwitchCheckedChange(isChecked: Boolean) {
        FoodSpotReportIntent.ChangeFoodTruckState(isChecked).post()
    }

    fun onClickIsOpenBtn(isOpen: Boolean) {
        FoodSpotReportIntent.ChangeOpenState(isOpen).post()
    }

    private fun FoodSpotReportIntent.post() =
        intent {
            when (this@post) {
                is FoodSpotReportIntent.ChangeFoodTruckState -> {
                    reduce {
                        state.copy(
                            isFoodTruck = isFoodTruck,
                        )
                    }
                }

                is FoodSpotReportIntent.ChangeOpenState -> {
                    reduce {
                        state.copy(
                            isOpen = isOpen,
                        )
                    }
                }
            }
        }
}
