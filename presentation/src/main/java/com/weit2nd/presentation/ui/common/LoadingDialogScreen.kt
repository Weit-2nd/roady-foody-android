package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialogScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = properties,
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
        ) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(16.dp),
            )
        }
    }
}
