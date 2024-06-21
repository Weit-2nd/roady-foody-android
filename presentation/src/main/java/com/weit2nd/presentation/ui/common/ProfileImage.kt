package com.weit2nd.presentation.ui.common

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.weit2nd.presentation.util.toPainter

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    defaultImage: Painter,
    onProfileImageClick: (() -> Unit) = {},
) {
    val context = LocalContext.current
    val painter = imgUri?.toPainter(context.contentResolver) ?: defaultImage

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onProfileImageClick() },
        contentScale = ContentScale.Crop,
    )
}
