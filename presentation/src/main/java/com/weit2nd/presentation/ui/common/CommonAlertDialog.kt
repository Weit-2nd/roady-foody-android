package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CommonAlertDialog(
    title: String,
    contents: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null,
    positiveButtonText: String = "확인",
    negativeButtonText: String = "취소",
    dialogProperties: DialogProperties = DialogProperties(),
) {
    CommonAlertDialogColumn(
        modifier = modifier,
        onDismiss = { onDismiss?.invoke() },
        dialogProperties = dialogProperties,
    ) {
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        TitleAndContents(
            title = title,
            contents = contents,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        CommonDialogButton(
            modifier = Modifier
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
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    onNegativeButtonClick()
                    onDismiss?.invoke()
                },
            ) {
                Text(
                    text = negativeButtonText,
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
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    onPositiveButtonClick()
                    onDismiss?.invoke()
                },
            ) {
                Text(
                    text = positiveButtonText,
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
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = contents,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CommonAlertDialogColumn(
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    dialogProperties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss?.invoke() },
        properties = dialogProperties,
    ) {
        Surface(
            modifier = modifier
                    .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            Column(
                modifier = Modifier.background(Color.White),
                content = content,
            )
        }
    }
}

@Preview
@Composable
fun CommonAlertDialogPreview() {
    CommonAlertDialog(
        title = "제목 제목",
        contents = "본문 본문\n본문 본문",
        onDismiss = { /* Do Nothing */ },
        onPositiveButtonClick = { },
        onNegativeButtonClick = { },
    )
}
