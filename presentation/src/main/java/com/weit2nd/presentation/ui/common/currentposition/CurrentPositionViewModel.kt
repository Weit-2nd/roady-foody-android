package com.weit2nd.presentation.ui.common.currentposition

import com.kakao.vectormap.LatLng
import com.weit2nd.domain.usecase.position.GetCurrentPositionUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CurrentPositionViewModel @Inject constructor(
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
) : BaseViewModel<CurrentPositionState, CurrentPositionSideEffect>() {
    override val container =
        container<CurrentPositionState, CurrentPositionSideEffect>(CurrentPositionState())

    private var currentPositionJob: Job =
        Job().apply {
            complete()
        }

    fun onButtonClick() {
        currentPositionJob.cancel()
        currentPositionJob = CurrentPositionIntent.RequestCurrentPosition.post()
    }

    private fun CurrentPositionIntent.post() =
        intent {
            when (this@post) {
                CurrentPositionIntent.RequestCurrentPosition -> {
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
                }
            }
        }
}
