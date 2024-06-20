package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

sealed class RatingStatus(
    open val color: Color,
    open val icon: ImageVector,
) {
    data class Filled(
        override val color: Color = Color(0xFFFFC700),
        override val icon: ImageVector = Icons.Filled.Star
    ) : RatingStatus(color, icon)

    data class HalfFilled(
        override val color: Color = Color(0xFFFFC700),
        override val icon: ImageVector = Icons.TwoTone.Star
    ) : RatingStatus(color, icon)

    data class Empty(
        override val color: Color = Color(0x20FFFFFF),
        override val icon: ImageVector = Icons.Filled.Star
    ) : RatingStatus(color, icon)
}

@Composable
fun StarRatingBar(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    rating: Float,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..maxStars) {
            val status = when {
                rating >= i -> RatingStatus.Filled()
                rating > i - 1 -> RatingStatus.HalfFilled()
                else -> RatingStatus.Empty()
            }
            Icon(
                imageVector = status.icon,
                contentDescription = null,
                tint = status.color,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
