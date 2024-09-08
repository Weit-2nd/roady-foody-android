package com.weit2nd.presentation.ui.common.currentposition

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.vectormap.LatLng
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CurrentPositionBtn(
    modifier: Modifier = Modifier,
    vm: CurrentPositionViewModel = hiltViewModel(),
    onClick: ((LatLng) -> Unit) = {},
    onError: (Throwable) -> Unit = {},
) {
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(sideEffect, onClick, onError)
    }
    CurrentPositionButton(
        modifier = modifier,
        onClick = vm::onButtonClick,
    )
}

@Composable
private fun CurrentPositionButton(
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 2.dp,
        pressedElevation = 2.dp,
        focusedElevation = 2.dp,
        hoveredElevation = 2.dp,
    ),
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.surface,
        elevation = elevation,
        contentColor = DarkGray,
    ) {
        Icon(
            modifier =
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_aim),
            contentDescription = "",
            tint = DarkGray,
        )
    }
}

private fun handleSideEffects(
    sideEffect: CurrentPositionSideEffect,
    onClick: (LatLng) -> Unit,
    onError: (Throwable) -> Unit,
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

@Preview
@Composable
private fun CurrentPositionButtonPreview() {
    RoadyFoodyTheme {
        CurrentPositionButton(
            modifier = Modifier.size(40.dp),
            onClick = {},
        )
    }
}
