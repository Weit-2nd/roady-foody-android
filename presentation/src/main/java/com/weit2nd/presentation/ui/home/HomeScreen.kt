package com.weit2nd.presentation.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
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
import com.weit2nd.domain.model.search.BusinessState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.ui.common.currentposition.CurrentPositionBtn
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.MarkerUtil
import com.weit2nd.presentation.util.getDistanceString
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    navToFoodSpotReport: () -> Unit,
    navToMyPage: () -> Unit,
    navToSearch: (PlaceSearchDTO) -> Unit,
    navToBack: () -> Unit,
    navToFoodSpotDetail: (Long) -> Unit,
) {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState =
                rememberStandardBottomSheetState(
                    initialValue = SheetValue.Hidden,
                    skipHiddenState = false,
                ),
        )
    var screenHeight by remember {
        mutableIntStateOf(0)
    }
    val statusBarPadding = WindowInsets.systemBars.asPaddingValues()
    val bottomSheetHeight by remember {
        derivedStateOf {
            with(localDensity) {
                runCatching {
                    val offset = screenHeight - bottomSheetScaffoldState.bottomSheetState.requireOffset()
                    (offset.toDp() - statusBarPadding.calculateBottomPadding()).coerceAtLeast(0.dp)
                }.getOrDefault(0.dp)
            }
        }
    }
    // 상태바 처리
    val colorScheme = MaterialTheme.colorScheme
    DisposableEffect(Unit) {
        val activity = context as ComponentActivity
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.TRANSPARENT // 상태바 투명화
        onDispose {
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            activity.window.statusBarColor = colorScheme.surface.toArgb()
        }
    }

    val state by vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            HomeSideEffect.NavToFoodSpotReport -> {
                navToFoodSpotReport()
            }
            is HomeSideEffect.NavToSearch -> {
                navToSearch(sideEffect.placeSearchDTO)
            }
            HomeSideEffect.NavToBack -> {
                navToBack()
            }
            is HomeSideEffect.NavToFoodSpotDetail -> {
                navToFoodSpotDetail(sideEffect.foodSpotId)
            }
            HomeSideEffect.NavToMyPage -> {
                navToMyPage()
            }
            is HomeSideEffect.RefreshMarkers -> {
                drawMarkers(context, sideEffect.map, sideEffect.foodSpotMarkers)
            }
            is HomeSideEffect.MoveCamera -> {
                moveCamera(sideEffect.map, sideEffect.position)
            }
            is HomeSideEffect.DeselectFoodSpot -> {
                removeMarker(sideEffect.map, sideEffect.foodSpotMarker.id)
                drawMarker(
                    markerIcon = MarkerUtil.getUnSelectedMarker(context),
                    map = sideEffect.map,
                    foodSpotMarker = sideEffect.foodSpotMarker,
                )
            }
            HomeSideEffect.ExpandFoodSpotSummary -> {
                bottomSheetScaffoldState.bottomSheetState.expand()
            }
            HomeSideEffect.HideFoodSpotSummary -> {
                bottomSheetScaffoldState.bottomSheetState.hide()
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
                        onCameraMoveEnd = vm::onCameraMoveEnd,
                        onMarkerClick = vm::onMarkerClick,
                        onMapClick = vm::onClickMap,
                        position = state.initialLatLng,
                    ),
                )
            }
        }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    LaunchedEffect(state.foodSpotMarkers) {
        state.map?.let {
            drawMarkers(context, it, state.foodSpotMarkers)
        }
    }

    LaunchedEffect(state.selectedFoodSpotMarker) {
        state.map?.let {
            val marker = state.selectedFoodSpotMarker ?: return@let
            removeMarker(it, marker.id)
            drawMarker(
                markerIcon = MarkerUtil.getSelectedMarker(context),
                map = it,
                foodSpotMarker = marker,
            )
        }
    }

    DisposableEffectWithLifeCycle(
        onResume = mapView::resume,
        onPause = mapView::pause,
    )

    BottomSheetScaffold(
        modifier =
            Modifier
                .onGloballyPositioned {
                    screenHeight = it.size.height
                },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            state.selectedFoodSpotMarker?.let {
                FoodSpotBottomSheetContent(
                    modifier =
                        Modifier
                            .padding(
                                start = 16.dp,
                                top = 4.dp,
                                end = 16.dp,
                                bottom = 16.dp + statusBarPadding.calculateBottomPadding(),
                            ).fillMaxWidth()
                            .clickable {
                                vm.onClickFoodSpotBottomSheet(it.id)
                            },
                    foodSpot = it,
                )
            }
        },
        sheetShape =
            RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
            ),
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight + statusBarPadding.calculateBottomPadding(),
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SearchBar(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface),
                    searchWords = state.searchWords,
                    profileImage = state.profileImage,
                    onSearchBarClick = vm::onSearchPlaceClick,
                    onProfileClick = vm::onProfileClick,
                )
                if (state.isMoved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    RetryButton(
                        onClick = {
                            state.map?.let { map ->
                                vm.onClickRefreshFoodSpotBtn(map)
                            }
                        },
                    )
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(bottom = bottomSheetHeight)
                        .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CurrentPositionBtn(
                    modifier = Modifier.size(40.dp),
                    onClick = vm::onClickCurrentPositionBtn,
                )
                FoodSpotReportButton(
                    onClick = vm::onClickReportBtn,
                )
            }
        }
    }
}

@Composable
private fun FoodSpotBottomSheetContent(
    modifier: Modifier = Modifier,
    foodSpot: FoodSpotMarker,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            FoodSpotName(
                name = foodSpot.name,
                isFoodTruck = foodSpot.isFoodTruck,
                category = foodSpot.category,
            )
            Spacer(modifier = Modifier.height(4.dp))
            FoodSpotReview(
                averageRate = foodSpot.averageRating,
                reviewCount = foodSpot.reviewCount,
            )
            Spacer(modifier = Modifier.height(4.dp))
            FoodSpotOperationHour(
                businessState = foodSpot.businessState,
                closeTime = foodSpot.closeTime,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text =
                    getDistanceString(
                        context = LocalContext.current,
                        distanceMeter = foodSpot.distance,
                    ),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            modifier =
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
            model = foodSpot.image,
            contentScale = ContentScale.Crop,
            contentDescription = "foodspot image",
            fallback = painterResource(id = R.drawable.ic_input_delete_filled),
        )
    }
}

@Composable
private fun FoodSpotName(
    modifier: Modifier = Modifier,
    name: String,
    isFoodTruck: Boolean,
    category: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (isFoodTruck) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_truck),
                contentDescription = "food truck",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = category,
            style = MaterialTheme.typography.labelMedium,
            color = Gray1,
        )
    }
}

@Composable
private fun FoodSpotReview(
    modifier: Modifier = Modifier,
    averageRate: Float,
    reviewCount: Int,
) {
    val averageRateText =
        if (reviewCount == 0) {
            stringResource(id = R.string.home_food_spot_no_review)
        } else {
            averageRate.toString()
        }
    val reviewCountText =
        if (reviewCount > 100) {
            stringResource(id = R.string.home_food_spot_review_count_over_100)
        } else {
            reviewCount.toString()
        }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            painter =
                painterResource(
                    id = R.drawable.ic_star,
                ),
            contentDescription = "average rate",
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = averageRateText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text =
                stringResource(
                    id = R.string.home_food_spot_review_count,
                    reviewCountText,
                ),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun FoodSpotOperationHour(
    modifier: Modifier = Modifier,
    businessState: BusinessState,
    closeTime: LocalTime,
) {
    val businessStateTextRes =
        when (businessState) {
            BusinessState.OPEN -> {
                R.string.home_food_spot_open
            }
            BusinessState.CLOSED -> {
                R.string.home_food_spot_closed
            }
            BusinessState.TEMPORARILY_CLOSED -> {
                R.string.home_food_spot_temporarily_closed
            }
            BusinessState.UNKNOWN -> {
                throw IllegalStateException()
            }
        }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = businessStateTextRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (businessState == BusinessState.OPEN) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text =
                    stringResource(
                        id = R.string.home_food_spot_close_time,
                        closeTime.hour,
                        closeTime.minute,
                    ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun FoodSpotReportButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors =
            ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Gray2,
            ),
        contentPadding = PaddingValues(8.dp),
    ) {
        Row {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.ic_pencil),
                contentDescription = "report foodSpot",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.home_post_food_spot),
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    searchWords: String,
    profileImage: String,
    onSearchBarClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val words =
                searchWords.ifBlank {
                    stringResource(id = R.string.home_search_bar_placeholder)
                }
            val textColor =
                if (searchWords.isNotBlank()) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    Gray1
                }
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .clickable {
                            onSearchBarClick()
                        },
                text = words,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                modifier =
                    Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            onProfileClick()
                        },
                model = profileImage,
                contentDescription = "navigate to profile",
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.ic_input_delete_filled),
            )
        }
    }
}

@Composable
fun RetryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        contentPadding =
            PaddingValues(
                start = 4.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = 8.dp,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_retry),
                contentDescription = "search",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.home_search_retry),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun drawMarkers(
    context: Context,
    map: KakaoMap,
    foodSpots: List<FoodSpotMarker>,
) {
    map.labelManager?.layer?.removeAll()
    foodSpots.forEach { marker ->
        drawMarker(
            markerIcon = MarkerUtil.getUnSelectedMarker(context),
            map = map,
            foodSpotMarker = marker,
        )
    }
}

private fun drawMarker(
    markerIcon: Bitmap,
    map: KakaoMap,
    foodSpotMarker: FoodSpotMarker,
) {
    val markerStyle =
        LabelStyle.from(markerIcon).apply {
            iconTransition = LabelTransition.from(Transition.None, Transition.None)
            anchorPoint = PointF(0.5f, 1.0f)
        }
    val options =
        LabelOptions
            .from(foodSpotMarker.position)
            .setStyles(LabelStyles.from(markerStyle))
            .apply {
                labelId = foodSpotMarker.id.toString()
            }
    map.labelManager?.layer?.addLabel(options)
}

private fun removeMarker(
    map: KakaoMap,
    foodSpotId: Long,
) {
    val layer = map.labelManager?.layer ?: return
    layer.getLabel(foodSpotId.toString())?.let { label ->
        layer.remove(label)
    }
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

@Composable
private fun DisposableEffectWithLifeCycle(
    // 오류로 compose.ui의 LocalLifecycleOwner 사용
    lifecycleOwner: LifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    onResume: () -> Unit,
    onPause: () -> Unit,
) {
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    currentOnResume()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    currentOnPause()
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

private fun kakaoMapReadyCallback(
    onMapReady: (KakaoMap) -> Unit,
    onCameraMoveEnd: (LatLng) -> Unit,
    onMarkerClick: (Long) -> Unit,
    onMapClick: () -> Unit,
    position: LatLng,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady(map)
        map.setOnCameraMoveEndListener { _, currentPosition, _ ->
            onCameraMoveEnd(currentPosition.position)
        }
        map.setOnLabelClickListener { _, _, label ->
            onMarkerClick(label.labelId.toLong())
        }
        map.setOnMapClickListener { _, _, _, _ ->
            onMapClick()
        }
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

@Preview
@Composable
private fun FoodSpotReportButtonPreview() {
    RoadyFoodyTheme {
        FoodSpotReportButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    RoadyFoodyTheme {
        SearchBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface),
            searchWords = "",
            profileImage = "",
            onSearchBarClick = {},
            onProfileClick = {},
        )
    }
}

@Preview
@Composable
private fun RetryButtonPreview() {
    RoadyFoodyTheme {
        RetryButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun FoodSpotBottomSheetContentPreview() {
    RoadyFoodyTheme {
        FoodSpotBottomSheetContent(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
            foodSpot =
                FoodSpotMarker(
                    id = 0,
                    name = "고양이 붕어빵",
                    image = "",
                    category = "붕어빵",
                    position = LatLng.from(0.0, 0.0),
                    distance = 100,
                    businessState = BusinessState.OPEN,
                    isFoodTruck = false,
                    averageRating = 4.8f,
                    reviewCount = 10,
                    closeTime = LocalTime.of(23, 0),
                ),
        )
    }
}
