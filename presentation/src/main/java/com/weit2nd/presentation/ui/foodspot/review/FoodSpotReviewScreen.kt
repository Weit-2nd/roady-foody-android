package com.weit2nd.presentation.ui.foodspot.review

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.spot.RatingCount
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO
import com.weit2nd.presentation.ui.common.FoodSpotImagePager
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.common.ReviewRequest
import com.weit2nd.presentation.ui.common.ReviewTotal
import com.weit2nd.presentation.ui.common.TitleAndCategory
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.Gray5
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodSpotReviewScreen(
    vm: FoodSpotReviewViewModel = hiltViewModel(),
    navToPostReview: (FoodSpotForReviewDTO) -> Unit,
) {
    val state by vm.collectAsState()
    val imagePagerState =
        rememberPagerState(
            pageCount = { state.foodSpotsPhotos.size },
        )
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FoodSpotReviewSideEffect.NavToPostReview -> {
                navToPostReview(sideEffect.foodSpotForReviewDTO)
            }
            FoodSpotReviewSideEffect.ShowNetworkErrorMessage -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.error_network),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }
    val lastVisibleItemIndex by
        remember {
            derivedStateOf {
                lazyListState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0
            }
        }
    val lazyListSize by
        remember {
            derivedStateOf {
                lazyListState.layoutInfo.totalItemsCount
            }
        }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    LaunchedEffect(lastVisibleItemIndex) {
        vm.onLastVisibleItemChanged(
            totalSize = lazyListSize,
            currentPosition = lastVisibleItemIndex,
        )
    }

    FoodSpotReviewContent(
        modifier = Modifier.fillMaxSize(),
        foodSpotReviewState = state,
        imagePagerState = imagePagerState,
        lazyListState = lazyListState,
        onImageClick = vm::onImageClick,
        onPostReviewClick = vm::onPostReviewClick,
        onReviewContentReadMoreClick = vm::onReviewContentsReadMoreClick,
        onReviewContentClick = vm::onReviewContentsClick,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FoodSpotReviewContent(
    modifier: Modifier = Modifier,
    foodSpotReviewState: FoodSpotReviewState,
    imagePagerState: PagerState,
    lazyListState: LazyListState,
    onImageClick: (position: Int) -> Unit,
    onPostReviewClick: () -> Unit,
    onReviewContentClick: (position: Int) -> Unit,
    onReviewContentReadMoreClick: (position: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            FoodSpotImagePager(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .heightIn(max = 500.dp),
                pagerState = imagePagerState,
                images = foodSpotReviewState.foodSpotsPhotos,
                onImageClick = { position ->
                    onImageClick(position)
                },
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray4,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            TitleAndCategory(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = foodSpotReviewState.name,
                isFoodTruck = foodSpotReviewState.movableFoodSpots,
                categories = foodSpotReviewState.categories,
            )
        }

        if (foodSpotReviewState.reviewCount > 0) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ReviewTotal(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                        ),
                    averageRating = foodSpotReviewState.averageRating,
                    reviewCount = foodSpotReviewState.reviewCount,
                    isReadMoreVisible = false,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            itemsIndexed(foodSpotReviewState.ratingCounts) { idx, ratingCount ->
                RatingRatio(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    ratingCount = ratingCount,
                    totalCount = foodSpotReviewState.reviewCount,
                )
                if (idx < foodSpotReviewState.ratingCounts.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            ReviewRequest(
                modifier = Modifier.fillMaxWidth(),
                onPostReviewClick = onPostReviewClick,
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        itemsIndexed(foodSpotReviewState.reviews) { idx, review ->
            ReviewItem(
                review = review.review,
                onImageClick = { _, position ->
                    onImageClick(position)
                },
                isContentExpended = review.isExpended,
                onContentClick = {
                    onReviewContentClick(idx)
                },
                onReadMoreClick = {
                    onReviewContentReadMoreClick(idx)
                },
            )
            if (idx < foodSpotReviewState.reviews.lastIndex) {
                HorizontalDivider(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                        ),
                    thickness = 1.dp,
                    color = Gray4,
                )
            }
        }
    }
}

@Composable
private fun RatingRatio(
    modifier: Modifier = Modifier,
    ratingCount: RatingCount,
    totalCount: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = ratingCount.rating.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .height(12.dp)
                    .weight(1f)
                    .background(Gray5),
        ) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxHeight()
                        .fillMaxWidth(ratingCount.count / totalCount.toFloat())
                        .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

@Preview
@Composable
private fun RatingRatioPreview() {
    RoadyFoodyTheme {
        RatingRatio(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
            ratingCount =
                RatingCount(
                    rating = 5,
                    count = 30,
                ),
            totalCount = 100,
        )
    }
}
