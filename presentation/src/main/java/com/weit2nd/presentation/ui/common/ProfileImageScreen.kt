package com.weit2nd.presentation.ui.common

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.weit2nd.presentation.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    onProfileImageClick: (() -> Unit) = {},
) {
    val context = LocalContext.current
    val painter = if (imgUri != null) {
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                imgUri
            )
        )
        BitmapPainter(bitmap.asImageBitmap())
    } else {
        // 기본 프로필 이미지
        painterResource(R.drawable.ic_launcher_background)
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .size(200.dp)
            .clickable { onProfileImageClick() },
        contentScale = ContentScale.Crop,
    )
}
