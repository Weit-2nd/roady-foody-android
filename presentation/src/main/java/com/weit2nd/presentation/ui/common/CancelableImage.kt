package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
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
        GlideImage(
            model = imgUri,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        IconButton(
            modifier =
                Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight(0.2f),
            onClick = { onDeleteImage(imgUri) },
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "cancel_button",
            )
        }
    }
}
