package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weit2nd.presentation.R

@Composable
fun CancelableImage(
    modifier: Modifier = Modifier,
    imgUri: String,
    onDeleteImage: ((String) -> Unit),
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
            model = imgUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = R.drawable.ic_input_delete_filled),
        )

        IconButton(
            modifier =
                Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .fillMaxWidth(0.25f)
                    .fillMaxHeight(0.25f),
            onClick = { onDeleteImage(imgUri) },
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.Filled.Close,
                tint = Color.White,
                contentDescription = "cancel_button",
            )
        }
    }
}
