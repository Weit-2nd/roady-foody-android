package com.weit2nd.presentation.ui.common.currentposition

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.vectormap.LatLng
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CurrentPositionBtn(
    modifier: Modifier = Modifier,
    vm: CurrentPositionViewModel = hiltViewModel(),
    onClick: ((LatLng) -> Unit) = {},
    onError: (Throwable) -> Unit = {},
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(sideEffect, onClick, onError)
    }
    Button(
        modifier = modifier,
        enabled = state.value.isLoading.not(),
        onClick = vm::onButtonClick,
    ) {
        Text(text = "현재 위치")
    }
}

private fun handleSideEffects(
    sideEffect: CurrentPositionSideEffect,
    onClick: (LatLng) -> Unit,
    onError: (Throwable) -> Unit
) {
    when (sideEffect) {
        is CurrentPositionSideEffect.PositionRequestSuccess -> {
            onClick(sideEffect.position)
        }

        is CurrentPositionSideEffect.PositionRequestFailed -> {
            onError(sideEffect.error)
        }
    }
}
