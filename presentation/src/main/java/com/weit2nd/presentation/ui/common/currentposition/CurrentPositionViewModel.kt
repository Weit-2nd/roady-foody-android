package com.weit2nd.presentation.ui.common.currentposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.usecase.position.GetCurrentPositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentPositionViewModel @Inject constructor(
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
) : ViewModel() {

    fun requestCurrentPosition(onClick: (LatLng) -> Unit) {
        viewModelScope.launch {
            val location = getCurrentPositionUseCase.invoke()
            val currentPosition = LatLng.from(
                location.latitude,
                location.longitude
            )
            onClick(currentPosition)
        }
    }
}
