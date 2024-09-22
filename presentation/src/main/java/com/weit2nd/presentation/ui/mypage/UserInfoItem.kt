package com.weit2nd.presentation.ui.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun UserInfoItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    textColor: Color,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InfoIcon(
            modifier =
                Modifier
                    .height(32.dp)
                    .width(68.dp),
            title = title,
            textColor = textColor,
            backgroundColor = backgroundColor,
            borderColor = borderColor,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.headlineSmall,
            color = DarkGray,
        )
    }
}

@Composable
private fun InfoIcon(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
) {
    Box(
        modifier =
            modifier
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = backgroundColor,
                ).border(BorderStroke(2.dp, borderColor), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun UserInfoPreview() {
    RoadyFoodyTheme {
        UserInfoItem(
            title = "뱃지",
            textColor = MaterialTheme.colorScheme.onPrimary,
            backgroundColor = MaterialTheme.colorScheme.primary,
            content = "초심자",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun InfoIconPreview() {
    RoadyFoodyTheme {
        InfoIcon(
            title = "랭킹",
            textColor = MaterialTheme.colorScheme.onPrimary,
            backgroundColor = MaterialTheme.colorScheme.primary,
        )
    }
}
