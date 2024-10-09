package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun CommonAlertDialog(
    title: String,
    contents: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null,
    positiveButtonText: String = stringResource(R.string.dialog_confirm),
    negativeButtonText: String = stringResource(R.string.dialog_cancel),
    dialogProperties: DialogProperties = DialogProperties(),
) {
    CommonDialogColumn(
        modifier = modifier,
        onDismiss = { onDismiss?.invoke() },
        dialogProperties = dialogProperties,
    ) {
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        TitleAndContents(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = title,
            contents = contents,
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        CommonDialogButton(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            onDismiss = onDismiss,
            onPositiveButtonClick = onPositiveButtonClick,
            onNegativeButtonClick = onNegativeButtonClick,
            positiveButtonText = positiveButtonText,
            negativeButtonText = negativeButtonText,
        )
    }
}

@Composable
private fun CommonDialogButton(
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null,
    positiveButtonText: String,
    negativeButtonText: String,
) {
    Row(
        modifier = modifier,
    ) {
        if (onNegativeButtonClick != null) {
            Button(
                modifier =
                    Modifier
                        .weight(1f),
                onClick = {
                    onNegativeButtonClick()
                    onDismiss?.invoke()
                },
                colors =
                    ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                border = BorderStroke(1.dp, Gray2),
            ) {
                Text(
                    text = negativeButtonText,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        if (onPositiveButtonClick != null && onNegativeButtonClick != null) {
            Spacer(
                modifier = Modifier.width(8.dp),
            )
        }
        if (onPositiveButtonClick != null) {
            Button(
                modifier =
                    Modifier
                        .weight(1f),
                onClick = {
                    onPositiveButtonClick()
                    onDismiss?.invoke()
                },
            ) {
                Text(
                    text = positiveButtonText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun TitleAndContents(
    title: String,
    contents: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = contents,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
fun CommonAlertDialogPreview() {
    RoadyFoodyTheme {
        CommonAlertDialog(
            title = "제목 제목",
            contents = "본문 본문본문 본문본문 본문본문 본문",
            onDismiss = { /* Do Nothing */ },
            onPositiveButtonClick = { },
            onNegativeButtonClick = { },
        )
    }
}
