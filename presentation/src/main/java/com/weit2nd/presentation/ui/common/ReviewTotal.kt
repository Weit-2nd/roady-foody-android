package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray2

@Composable
fun ReviewTotal(
    modifier: Modifier = Modifier,
    averageRating: Float,
    reviewCount: Int,
    onClick: (() -> Unit)? = null,
    isReadMoreVisible: Boolean,
) {
    Row(
        modifier =
            modifier.clickable {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "review rating",
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = averageRating.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text =
                stringResource(
                    id = R.string.food_spot_detail_review_count,
                    reviewCount,
                ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (isReadMoreVisible) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "navigate to review detail",
                tint = Gray2,
            )
        }
    }
}
