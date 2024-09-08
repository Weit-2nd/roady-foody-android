package com.weit2nd.presentation.ui.foodspot.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotOpenState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.OperationHour
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.ui.common.BorderButton
import com.weit2nd.presentation.ui.common.CommonTopBar
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.Gray5
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FoodSpotDetailScreen(
    vm: FoodSpotDetailViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state by vm.collectAsState()

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            FoodSpotDetailSideEffect.NavToBack -> {
                navToBack()
            }
        }
    }

    val isViewMoreOperationHoursEnabled by remember {
        derivedStateOf {
            (
                state.openState == FoodSpotOpenState.CLOSED ||
                    state.openState == FoodSpotOpenState.OPEN
            ) &&
                state.operationHours.isNotEmpty() &&
                state.isOperationHoursOpen.not()
        }
    }
    val isBusinessInformationShow by remember {
        derivedStateOf {
            state.openState != FoodSpotOpenState.UNKNOWN
        }
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                modifier = Modifier.fillMaxWidth(),
                onNavigationButtonClick = vm::onNavigationButtonClick,
            )
        },
    ) {
        FoodSpotDetailContent(
            modifier = Modifier.padding(it),
            state = state,
            isViewMoreOperationHoursEnabled = isViewMoreOperationHoursEnabled,
            isBusinessInformationShow = isBusinessInformationShow,
            onImageClick = vm::onImageClick,
            onOperationHourClick = vm::onOperationHourClick,
            onPostReviewClick = vm::onPostReviewClick,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FoodSpotDetailContent(
    modifier: Modifier = Modifier,
    state: FoodSpotDetailState,
    isViewMoreOperationHoursEnabled: Boolean,
    isBusinessInformationShow: Boolean,
    onImageClick: (images: List<String>, position: Int) -> Unit,
    onOperationHourClick: (Boolean) -> Unit,
    onPostReviewClick: () -> Unit,
) {
    val imagePagerState =
        rememberPagerState(
            pageCount = { state.foodSpotsPhotos.size },
        )
    val horizontalPadding = 16.dp
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            HorizontalDivider(
                thickness = 1.dp,
            )
            FoodSpotImagePager(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .heightIn(max = 500.dp),
                pagerState = imagePagerState,
                images = state.foodSpotsPhotos,
                onImageClick = { position ->
                    onImageClick(state.foodSpotsPhotos, position)
                },
            )
            HorizontalDivider(
                thickness = 1.dp,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            TitleAndCategory(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                title = state.name,
                categories = state.foodCategoryList,
            )
        }
        if (isBusinessInformationShow) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                FoodSpotBusinessInformation(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding)
                            .clickable {
                                onOperationHourClick(state.isOperationHoursOpen)
                            },
                    openState = state.openState,
                    isViewMoreEnabled = isViewMoreOperationHoursEnabled,
                )
            }
        }
        if (state.isOperationHoursOpen) {
            itemsIndexed(state.operationHours) { idx, operationHour ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding)
                            .clickable {
                                onOperationHourClick(true)
                            },
                ) {
                    if (idx == 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    OperationHour(
                        operationHour = operationHour,
                    )
                    if (idx < state.operationHours.lastIndex) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
        item {
            HorizontalDivider(
                thickness = 8.dp,
                color = Gray5,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ReviewTotal(
                modifier =
                    Modifier.padding(
                        horizontal = 16.dp,
                    ),
                averageRating = 4.8f,
                reviewCount = 256,
                onClick = {},
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReviewRequest(
                modifier = Modifier.fillMaxWidth(),
                onPostReviewClick = onPostReviewClick,
            )
        }
        itemsIndexed(state.reviews) { idx, review ->
            ReviewItem(
                review = review,
                onImageClick = onImageClick,
            )
            if (idx < state.reviews.lastIndex) {
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
        if (state.isViewMoreReviewVisible) {
            item {
                BorderButton(
                    text = stringResource(id = R.string.food_spot_detail_view_more_review),
                    onClick = {
                        // TODO 리뷰 더보기 화면으로 이동
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun FoodSpotImagePager(
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
            contentScale = ContentScale.Fit,
            fallback = painterResource(id = R.drawable.ic_input_delete_filled),
        )
    }
}

@Composable
private fun TitleAndCategory(
    modifier: Modifier = Modifier,
    title: String,
    categories: List<FoodCategory>,
) {
    val categoriseText =
        categories.joinToString(", ") {
            it.name
        }
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 21.sp,
        )
        if (categories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = categoriseText,
                color = Color.Gray,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun FoodSpotReviews(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
    contentPadding: PaddingValues,
    onImageClick: (images: List<String>, position: Int) -> Unit,
    onPostReviewClick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        ReviewTotal(
            averageRating = 4.8f,
            reviewCount = 256,
            onClick = {},
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReviewRequest(
            modifier =
                Modifier
                    .fillMaxWidth(),
            onPostReviewClick = onPostReviewClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = contentPadding,
        ) {
            itemsIndexed(reviews) { idx, review ->
                ReviewItem(
                    review = review,
                    onImageClick = onImageClick,
                )
                if (idx < reviews.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ReviewTotal(
    modifier: Modifier = Modifier,
    averageRating: Float,
    reviewCount: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier.clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "review rating",
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = averageRating.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text =
                stringResource(
                    id = R.string.food_spot_detail_review_count,
                    reviewCount,
                ),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "navigate to review detail",
            tint = Gray2,
        )
    }
}

@Composable
private fun ReviewRequest(
    modifier: Modifier = Modifier,
    onPostReviewClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.food_spot_detail_post_review_description),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        BorderButton(
            text = stringResource(id = R.string.food_spot_detail_post_review_button),
            onClick = onPostReviewClick,
        )
    }
}

@Composable
private fun FoodSpotBusinessInformation(
    modifier: Modifier = Modifier,
    openState: FoodSpotOpenState,
    isViewMoreEnabled: Boolean,
) {
    val openStateTextRes = openState.getStringRes()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = "",
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = openStateTextRes),
            fontSize = 18.sp,
            color = Color.Black,
        )
        if (isViewMoreEnabled) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_arrow_bottom),
                contentDescription = "",
                tint = Color.Unspecified,
            )
        }
    }
}

private fun FoodSpotOpenState.getStringRes() =
    when (this) {
        FoodSpotOpenState.OPEN -> R.string.food_spot_detail_open
        FoodSpotOpenState.CLOSED -> R.string.food_spot_detail_closed
        FoodSpotOpenState.TEMPORARILY_CLOSED -> R.string.food_spot_detail_temporarily_closed
        FoodSpotOpenState.UNKNOWN -> throw IllegalArgumentException()
    }

@Composable
private fun OperationHours(
    modifier: Modifier = Modifier,
    operationHours: List<OperationHour>,
) {
    Column(
        modifier = modifier,
    ) {
        operationHours.forEach {
            Text(
                text =
                    stringResource(
                        id = R.string.food_spot_detail_operation_hours,
                        it.dayOfWeek,
                        it.open,
                        it.close,
                    ),
                color = Color.Black,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
fun OperationHour(
    modifier: Modifier = Modifier,
    operationHour: OperationHour,
) {
    Text(
        modifier = modifier,
        text =
            stringResource(
                id = R.string.food_spot_detail_operation_hours,
                operationHour.dayOfWeek,
                operationHour.open,
                operationHour.close,
            ),
        color = Color.Black,
        fontSize = 18.sp,
    )
}

@Preview
@Composable
private fun FoodSpotDetailPreview() {
    RoadyFoodyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            FoodSpotDetailContent(
                state =
                    FoodSpotDetailState(
                        name = "빵빵하게",
                        foodCategoryList =
                            listOf(
                                FoodCategory(
                                    0,
                                    "붕어빵",
                                ),
                                FoodCategory(
                                    1,
                                    "광어빵",
                                ),
                            ),
                        foodSpotsPhotos = listOf("a"),
                    ),
                isViewMoreOperationHoursEnabled = true,
                isBusinessInformationShow = false,
                onImageClick = { _, _ -> },
                onOperationHourClick = {},
                onPostReviewClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TitleAndCategoryPreview() {
    Surface(
        modifier = Modifier.background(Color.White),
    ) {
        TitleAndCategory(
            title = "빵빵하게",
            categories =
                listOf(
                    FoodCategory(
                        0,
                        "붕어빵",
                    ),
                    FoodCategory(
                        1,
                        "광어빵",
                    ),
                ),
        )
    }
}
