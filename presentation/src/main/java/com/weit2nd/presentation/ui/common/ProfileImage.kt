package com.weit2nd.presentation.ui.common

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri,
    onProfileImageClick: (() -> Unit) = {},
) {
    val context = LocalContext.current
    val painter = run {
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                imgUri
            )
        )
        BitmapPainter(bitmap.asImageBitmap())
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onProfileImageClick() },
        contentScale = ContentScale.Crop,
    )
}
