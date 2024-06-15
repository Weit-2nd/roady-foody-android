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
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CurrentPositionSideEffect.OnClick -> {
                onClick(sideEffect.position)
            }
        }
    }
    Button(
        modifier = modifier,
        enabled = state.value.isLoading.not(),
        onClick = vm::onButtonClick,
    ) {
        Text(text = "현재 위치")
    }
}
