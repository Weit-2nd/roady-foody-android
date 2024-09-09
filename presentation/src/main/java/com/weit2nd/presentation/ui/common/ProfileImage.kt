package com.weit2nd.presentation.ui.common

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.Gray5
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.ui.theme.White

@Composable
fun EditableProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    onProfileImageClick: (() -> Unit) = {},
) {
    Box(
        modifier = modifier.padding(8.dp),
    ) {
        ProfileImage(
            modifier = modifier.fillMaxSize(),
            imgUri = imgUri,
            onProfileImageClick = onProfileImageClick,
        )
        EditButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onProfileImageClick() },
        )
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    onProfileImageClick: (() -> Unit) = {},
) {
    var isImageLoading by remember { mutableStateOf(true) }

    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .clickable { onProfileImageClick() },
    ) {
        AsyncImage(
            modifier = modifier.fillMaxSize(),
            model = imgUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onSuccess = { isImageLoading = false },
        )

        if (isImageLoading) {
            DefaultProfileImage(modifier = modifier.fillMaxSize())
        }
    }
}

@Composable
private fun DefaultProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .border(1.dp, Gray4, shape = CircleShape)
                .background(Gray5)
                .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            painter = painterResource(id = R.drawable.ic_default_profile),
            contentDescription = "",
        )
    }
}

@Composable
private fun EditButton(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .shadow(2.dp, CircleShape)
                .fillMaxSize(0.35f)
                .clip(CircleShape)
                .background(White),
    ) {
        Icon(
            modifier =
                Modifier
                    .fillMaxSize(0.7f)
                    .align(Alignment.Center)
                    .size(24.dp),
            imageVector = Icons.Outlined.Edit,
            contentDescription = "",
            tint = Black,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DefaultProfileImagePreview() {
    RoadyFoodyTheme {
        DefaultProfileImage(
            Modifier
                .size(100.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EditableProfileImagePreview() {
    RoadyFoodyTheme {
        EditableProfileImage(
            Modifier
                .size(100.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EditButtonPreview() {
    RoadyFoodyTheme {
        EditButton(modifier = Modifier.size(32.dp))
    }
}
