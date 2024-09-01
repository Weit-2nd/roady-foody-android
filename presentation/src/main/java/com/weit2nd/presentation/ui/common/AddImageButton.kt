package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun AddImageButton(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .size(80.dp)
                .border(
                    1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier =
                    Modifier
                        .size(32.dp),
                painter = painterResource(id = R.drawable.ic_add_image),
                contentDescription = "",
            )
            Text(
                text = "사진등록",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
fun AddImageButtonPreview() {
    RoadyFoodyTheme {
        AddImageButton()
    }
}
