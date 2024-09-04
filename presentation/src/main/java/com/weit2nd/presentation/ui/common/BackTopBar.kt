package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onClickBackBtn: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Black,
        )
        IconButton(
            modifier = Modifier.padding(start = 4.dp),
            onClick = { onClickBackBtn() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_navigate),
                contentDescription = "",
                tint = Black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    RoadyFoodyTheme {
        BackTopBar(modifier = Modifier.fillMaxWidth(), title = "Title") {}
    }
}
