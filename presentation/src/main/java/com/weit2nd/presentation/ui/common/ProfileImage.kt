package com.weit2nd.presentation.ui.common

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.weit2nd.presentation.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    defaultImage: Int = R.drawable.ic_launcher_background,
    onProfileImageClick: (() -> Unit) = {},
) {
    GlideImage(
        model = imgUri,
        contentDescription = "ProfileImage",
        modifier =
            modifier
                .clip(CircleShape)
                .clickable { onProfileImageClick() },
        contentScale = ContentScale.Crop,
    ) {
        it.fallback(defaultImage)
    }
}
