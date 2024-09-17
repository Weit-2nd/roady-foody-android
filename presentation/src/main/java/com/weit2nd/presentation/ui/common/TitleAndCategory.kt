package com.weit2nd.presentation.ui.common

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray1

@Composable
fun TitleAndCategory(
    modifier: Modifier = Modifier,
    title: String,
    isFoodTruck: Boolean,
    categories: List<FoodCategory>,
) {
    val categoriseText =
        categories.joinToString(", ") {
            it.name
        }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        if (isFoodTruck) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_truck),
                contentDescription = "food truck",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        if (categories.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = categoriseText,
                style = MaterialTheme.typography.labelLarge,
                color = Gray1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
