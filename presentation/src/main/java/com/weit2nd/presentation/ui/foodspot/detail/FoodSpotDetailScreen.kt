package com.weit2nd.presentation.ui.foodspot.detail

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTransition
import com.kakao.vectormap.label.Transition
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotOpenState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.OperationHour
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO
import com.weit2nd.presentation.navigation.dto.FoodSpotReviewDTO
import com.weit2nd.presentation.navigation.dto.ImageViewerDTO
import com.weit2nd.presentation.ui.common.BorderButton
import com.weit2nd.presentation.ui.common.FoodSpotImagePager
import com.weit2nd.presentation.ui.common.LoadingDialogScreen
import com.weit2nd.presentation.ui.common.RetryScreen
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.common.ReviewRequest
import com.weit2nd.presentation.ui.common.ReviewTotal
import com.weit2nd.presentation.ui.common.TitleAndCategory
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.Gray5
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.DisposableEffectWithLifeCycle
import com.weit2nd.presentation.util.MarkerUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalTime

@Composable
fun FoodSpotDetailScreen(
    vm: FoodSpotDetailViewModel = hiltViewModel(),
    navToBack: () -> Unit,
    navToPostReview: (FoodSpotForReviewDTO) -> Unit,
    navToFoodSpotReview: (FoodSpotReviewDTO) -> Unit,
    navToImageViewer: (ImageViewerDTO) -> Unit,
) {
    val state by vm.collectAsState()

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            FoodSpotDetailSideEffect.NavToBack -> {
                navToBack()
            }

            is FoodSpotDetailSideEffect.MoveAndDrawMarker -> {
                moveCamera(
                    map = sideEffect.map,
                    position = sideEffect.position,
                )
                drawMarker(
                    context = context,
                    map = sideEffect.map,
                    position = sideEffect.position,
                )
            }

            is FoodSpotDetailSideEffect.NavToPostReview -> {
                navToPostReview(sideEffect.foodSpotForReviewDTO)
            }

            is FoodSpotDetailSideEffect.NavToFoodSpotReview -> {
                navToFoodSpotReview(sideEffect.foodSpotReviewDTO)
            }

            is FoodSpotDetailSideEffect.NavToImageViewer -> {
                navToImageViewer(sideEffect.imageViewerDTO)
            }
        }
    }
    val mapView =
        remember {
            MapView(context).apply {
                start(
                    mapLifeCycleCallback(),
                    kakaoMapReadyCallback(
                        onMapReady = vm::onMapReady,
                        position = state.position,
                    ),
                )
            }
        }

    DisposableEffectWithLifeCycle(
        onResume = mapView::resume,
        onPause = mapView::pause,
    )

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
    Box {
        if (state.isLoading) {
            LoadingDialogScreen()
        }
        if (state.shouldRetry) {
            RetryScreen(
                modifier = Modifier.fillMaxSize(),
                onRetryButtonClick = vm::onRetryButtonClick,
            )
        } else {
            FoodSpotDetailContent(
                state = state,
                mapView = mapView,
                isViewMoreOperationHoursEnabled = isViewMoreOperationHoursEnabled,
                isBusinessInformationShow = isBusinessInformationShow,
                onImageClick = vm::onImageClick,
                onOperationHourClick = vm::onOperationHourClick,
                onPostReviewClick = vm::onPostReviewClick,
                onReviewContentClick = vm::onReviewContentsClick,
                onReviewContentReadMoreClick = vm::onReviewContentsReadMoreClick,
                onReviewReadMoreClick = vm::onReviewReadMoreClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FoodSpotDetailContent(
    modifier: Modifier = Modifier,
    state: FoodSpotDetailState,
    mapView: MapView,
    isViewMoreOperationHoursEnabled: Boolean,
    isBusinessInformationShow: Boolean,
    onImageClick: (images: List<String>, position: Int) -> Unit,
    onOperationHourClick: (Boolean) -> Unit,
    onPostReviewClick: () -> Unit,
    onReviewContentClick: (position: Int) -> Unit,
    onReviewContentReadMoreClick: (position: Int) -> Unit,
    onReviewReadMoreClick: () -> Unit,
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
                color = Gray4,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            TitleAndCategory(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                title = state.name,
                isFoodTruck = state.movableFoodSpots,
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
                    closedTime = state.todayCloseTime,
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
        if (state.address.isNotBlank()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                FoodSpotAddress(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    address = state.address,
                )
                Spacer(modifier = Modifier.height(8.dp))
                AndroidView(
                    modifier =
                        Modifier
                            .padding(horizontal = horizontalPadding)
                            .clip(RoundedCornerShape(12.dp))
                            .aspectRatio(4f / 3f)
                            .fillMaxWidth(),
                    factory = {
                        (mapView.parent as? ViewGroup)?.removeView(mapView)
                        mapView
                    },
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                thickness = 8.dp,
                color = Gray5,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if (state.reviewCount > 0) {
                ReviewTotal(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                        ),
                    averageRating = state.averageRating,
                    reviewCount = state.reviewCount,
                    onClick = onReviewReadMoreClick,
                    isReadMoreVisible = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            // TODO 내가 쓴 리뷰가 없을 때만 뜨게 하고 싶음....
            ReviewRequest(
                modifier = Modifier.fillMaxWidth(),
                onPostReviewClick = onPostReviewClick,
            )
        }
        itemsIndexed(state.reviews) { idx, review ->
            ReviewItem(
                review = review.review,
                onImageClick = onImageClick,
                isContentExpended = review.isExpended,
                onContentClick = {
                    onReviewContentClick(idx)
                },
                onReadMoreClick = {
                    onReviewContentReadMoreClick(idx)
                },
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
        if (state.hasMoreReviews) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    BorderButton(
                        text = stringResource(id = R.string.food_spot_detail_view_more_review),
                        onClick = onReviewReadMoreClick,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun FoodSpotBusinessInformation(
    modifier: Modifier = Modifier,
    openState: FoodSpotOpenState,
    closedTime: LocalTime?,
    isViewMoreEnabled: Boolean,
) {
    val openStateTextRes = openState.getStringRes()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            tint = Gray2,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = openStateTextRes),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (openState == FoodSpotOpenState.OPEN && closedTime != null) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text =
                    stringResource(
                        id = R.string.food_spot_detail_operation_hour_close_time,
                        closedTime.hour,
                        closedTime.minute,
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        if (isViewMoreEnabled) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_arrow_bottom),
                contentDescription = "view more",
                tint = Gray2,
            )
        }
    }
}

@Composable
fun FoodSpotAddress(
    modifier: Modifier = Modifier,
    address: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.ic_marker_address),
            contentDescription = null,
            tint = Gray2,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = address,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
private fun OperationHour(
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
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

private fun mapLifeCycleCallback() =
    object : MapLifeCycleCallback() {
        override fun onMapDestroy() {
            Log.d("KakaoMap", "onMapDestroy")
        }

        override fun onMapError(error: Exception) {
            Log.e("KakaoMap", "onMapError: ", error)
        }
    }

private fun kakaoMapReadyCallback(
    onMapReady: (KakaoMap) -> Unit,
    position: LatLng,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady(map)
    }

    override fun getPosition(): LatLng = position
}

private fun moveCamera(
    map: KakaoMap,
    position: LatLng,
) {
    val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
    map.moveCamera(cameraUpdate)
}

private fun drawMarker(
    context: Context,
    map: KakaoMap,
    position: LatLng,
) {
    val markerStyle =
        LabelStyle.from(MarkerUtil.getSelectedMarker(context)).apply {
            iconTransition = LabelTransition.from(Transition.None, Transition.None)
            anchorPoint = PointF(0.5f, 1.0f)
        }
    val options =
        LabelOptions
            .from(position)
            .setStyles(LabelStyles.from(markerStyle))
            .apply {
                labelId = position.toString()
            }
    map.labelManager?.layer?.addLabel(options)
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
                        openState = FoodSpotOpenState.OPEN,
                        movableFoodSpots = true,
                        foodSpotsPhotos = listOf("a"),
                    ),
                mapView = MapView(LocalContext.current),
                isViewMoreOperationHoursEnabled = true,
                isBusinessInformationShow = false,
                onImageClick = { _, _ -> },
                onOperationHourClick = {},
                onPostReviewClick = {},
                onReviewContentClick = {},
                onReviewContentReadMoreClick = {},
                onReviewReadMoreClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TitleAndCategoryPreview() {
    RoadyFoodyTheme {
        Surface(
            modifier = Modifier.background(Color.White),
        ) {
            TitleAndCategory(
                title = "빵빵하게",
                isFoodTruck = true,
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
}

@Preview
@Composable
private fun FoodSpotBusinessInformationPreview() {
    RoadyFoodyTheme {
        FoodSpotBusinessInformation(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            openState = FoodSpotOpenState.OPEN,
            closedTime = LocalTime.of(22, 0),
            isViewMoreEnabled = true,
        )
    }
}
