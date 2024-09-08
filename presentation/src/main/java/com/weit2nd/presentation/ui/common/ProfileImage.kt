package com.weit2nd.presentation.ui.common

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray4

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    @DrawableRes fallback: Int = R.drawable.ic_launcher_background,
    placeholder: Painter? = ColorPainter(Gray4),
    onProfileImageClick: (() -> Unit) = {},
) {
    AsyncImage(
        model = imgUri,
        contentDescription = "ProfileImage",
        modifier =
            modifier
                .clip(CircleShape)
                .clickable { onProfileImageClick() },
        contentScale = ContentScale.Crop,
        fallback = painterResource(id = fallback),
        placeholder = placeholder,
    )
}
