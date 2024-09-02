package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun BottomButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = { onClick() },
        enabled = enabled,
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportButtonPreview() {
    RoadyFoodyTheme {
        BottomButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { },
            enabled = true,
            text = "음식점 등록하기",
        )
    }
}
