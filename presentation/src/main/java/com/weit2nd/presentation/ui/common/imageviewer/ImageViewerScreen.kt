package com.weit2nd.presentation.ui.common.imageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImageViewerScreen(
    vm: ImageViewerViewModel = hiltViewModel(),
    onExitBtnClick: () -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ImageViewerSideEffect.ExitImageViewer -> {
                onExitBtnClick()
            }
        }
    }

    val pagerState =
        rememberPagerState(
            pageCount = { state.value.images.size },
            initialPage = state.value.position,
        )
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        HorizontalPager(state = pagerState) { page ->
            GlideImage(
                model = state.value.images[page],
                contentDescription = "$page page Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
        PageIndicator(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
            pagerState = pagerState,
        )
        IconButton(
            modifier =
                Modifier
                    .padding(4.dp)
                    .align(Alignment.TopStart),
            onClick = vm::onClickExitButton,
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "exitPage",
                tint = Color.White,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { index ->
            val color =
                if (pagerState.currentPage == index) Color.White else Color.DarkGray
            Box(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp),
            )
        }
    }
}
