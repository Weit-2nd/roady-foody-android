package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun TitleTopBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun TopBarPreview() {
    RoadyFoodyTheme {
        TitleTopBar(
            modifier = Modifier.fillMaxWidth(),
            title = "Title",
        )
    }
}
