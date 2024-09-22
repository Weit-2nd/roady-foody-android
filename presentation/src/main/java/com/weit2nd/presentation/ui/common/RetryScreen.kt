package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RetryScreen(
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit,
) {
    // TODO 디자인 적용
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onRetryButtonClick,
        ) {
            Text(text = "재시도")
        }
    }
}
