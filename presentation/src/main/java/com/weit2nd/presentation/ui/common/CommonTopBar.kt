package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.R

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigationButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onNavigationButtonClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_navigate),
                contentDescription = "",
            )
        }
        if (title != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = Color.Black,
            )
        }
    }
}
