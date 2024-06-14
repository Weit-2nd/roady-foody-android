package com.weit2nd.presentation.ui.common.currentposition

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.vectormap.LatLng

@Composable
fun CurrentPositionBtn(
    modifier: Modifier = Modifier,
    vm: CurrentPositionViewModel = hiltViewModel(),
    onClick: ((LatLng) -> Unit) = {},
) {
    Button(
        modifier = modifier,
        onClick = {
            vm.requestCurrentPosition { onClick(it) }
        },
    ) {
        Text(text = "현재 위치")
    }
}
