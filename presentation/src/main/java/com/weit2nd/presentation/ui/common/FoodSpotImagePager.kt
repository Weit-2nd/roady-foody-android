package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray4

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodSpotImagePager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    images: List<String>,
    onImageClick: (Int) -> Unit,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        AsyncImage(
            modifier =
            Modifier
                .fillMaxSize()
                .clickable {
                    onImageClick(page)
                },
            model = images[page],
            contentDescription = "foodSpotImage$page",
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = R.drawable.ic_input_delete_filled),
            placeholder = ColorPainter(Gray4),
        )
    }
}
