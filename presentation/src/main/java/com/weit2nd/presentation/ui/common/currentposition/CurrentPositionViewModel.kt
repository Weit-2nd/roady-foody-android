package com.weit2nd.presentation.ui.common.currentposition

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.usecase.position.GetCurrentPositionUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CurrentPositionViewModel @Inject constructor(
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
) : BaseViewModel<CurrentPositionState, CurrentPositionSideEffect>() {
    override val container =
        container<CurrentPositionState, CurrentPositionSideEffect>(CurrentPositionState())

    fun onButtonClick() {
        CurrentPositionIntent.RequestCurrentPosition.post()
    }

    private fun CurrentPositionIntent.post() =
        intent {
            when (this@post) {
                CurrentPositionIntent.RequestCurrentPosition -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }
                    runCatching {
                        val location = getCurrentPositionUseCase.invoke()
                        val currentPosition =
                            LatLng.from(
                                location.latitude,
                                location.longitude,
                            )
                        postSideEffect(CurrentPositionSideEffect.PositionRequestSuccess(currentPosition))
                    }.onFailure {
                        postSideEffect(CurrentPositionSideEffect.PositionRequestFailed(it))
                    }
                    reduce {
                        state.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
}
