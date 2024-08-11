package com.weit2nd.presentation.ui.foodspot.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotOpenState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.OperationHour
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.ui.common.ReviewItem
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun FoodSpotDetailScreen(vm: FoodSpotDetailViewModel = hiltViewModel()) {
    val state by vm.collectAsState()

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Scaffold(
        topBar = {
        },
    ) {
        FoodSpotDetailContent(
            modifier = Modifier.padding(it),
            state = state,
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
    onImageClick: (images: List<String>, position: Int) -> Unit,
    onOperationHourClick: (Boolean) -> Unit,
    onPostReviewClick: () -> Unit,
) {
    val isViewMoreOperationHoursEnabled by remember {
        derivedStateOf {
            state.openState == FoodSpotOpenState.OPEN &&
                state.operationHours.isNotEmpty() &&
                state.isOperationHoursOpen.not()
        }
    }
    val isBusinessInformationShow by remember {
        derivedStateOf {
            state.openState != FoodSpotOpenState.UNKNOWN
        }
    }
    val imagePagerState =
        rememberPagerState(
            pageCount = { state.foodSpotsPhotos.size },
        )
    val horizontalPadding = 16.dp
    LazyColumn(
        modifier = modifier,
    ) {
        item {
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
            Spacer(modifier = Modifier.height(8.dp))
            FoodSpotReviews(
                modifier = Modifier.fillMaxWidth(),
                reviews = state.reviews,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                onImageClick = onImageClick,
                onPostReviewClick = onPostReviewClick,
            )
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
        GlideImage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable {
                        onImageClick(page)
                    },
            model = images[page],
            contentDescription = "foodSpotImage$page",
            contentScale = ContentScale.Fit,
            loading = placeholder(painterResource(id = R.drawable.ic_input_delete_filled)),
            failure = placeholder(painterResource(id = R.drawable.ic_input_delete_filled)),
        )
    }
}

@Composable
private fun TitleAndCategory(
    modifier: Modifier = Modifier,
    title: String,
    categories: List<FoodCategory>,
) {
    val isCategoryVisible by remember {
        derivedStateOf {
            categories.isNotEmpty()
        }
    }
    val categoriseText by remember {
        derivedStateOf {
            categories.joinToString(", ") {
                it.name
            }
        }
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
        if (isCategoryVisible) {
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
        Text(
            modifier = Modifier.padding(contentPadding),
            text = "방문자 리뷰",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 21.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (reviews.isNotEmpty()) {
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
        } else {
            EmptyReview(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                onPostReviewClick = onPostReviewClick,
            )
        }
    }
}

@Composable
fun EmptyReview(
    modifier: Modifier = Modifier,
    onPostReviewClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "리뷰가 옵소요..",
            color = Color.Black,
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onPostReviewClick,
        ) {
            Text(
                text = "리뷰 작성하러 가기",
            )
        }
    }
}

@Composable
private fun FoodSpotBusinessInformation(
    modifier: Modifier = Modifier,
    openState: FoodSpotOpenState,
    isViewMoreEnabled: Boolean,
) {
    val openStateTextRes by remember {
        derivedStateOf {
            openState.getStringRes()
        }
    }
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
            onImageClick = { _, _ -> },
            onOperationHourClick = {},
            onPostReviewClick = {},
        )
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
