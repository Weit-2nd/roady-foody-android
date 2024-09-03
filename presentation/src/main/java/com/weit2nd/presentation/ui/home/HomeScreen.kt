package com.weit2nd.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.ui.common.currentposition.CurrentPositionBtn
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    navToFoodSpotReport: () -> Unit,
    navToMyPage: () -> Unit,
    navToSearch: (PlaceSearchDTO) -> Unit,
    navToBack: () -> Unit,
    navToFoodSpotDetail: (Long) -> Unit,
) {
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
                drawMarkers(sideEffect.map, sideEffect.foodSpotMarkers)
            }
            is HomeSideEffect.MoveCamera -> {
                moveCamera(sideEffect.map, sideEffect.position)
            }
        }
    }

    val context = LocalContext.current
    val mapView =
        remember {
            MapView(context).apply {
                start(
                    mapLifeCycleCallback(),
                    kakaoMapReadyCallback(
                        onMapReady = vm::onMapReady,
                        onCameraMoveEnd = vm::onCameraMoveEnd,
                        onMarkerClick = vm::onMarkerClick,
                        position = state.initialLatLng,
                    ),
                )
            }
        }

    LaunchedEffect(state.foodSpots) {
        state.map?.let {
            drawMarkers(it, state.foodSpots)
        }
    }

    DisposableEffectWithLifeCycle(
        onResume = mapView::resume,
        onPause = mapView::pause,
    )

    Scaffold {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { mapView },
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
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
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                state.map?.let { map -> vm.onClickRefreshFoodSpotBtn(map) }
                            },
                        ) {
                            Text(text = "이 위치에서 검색")
                        }
                    }
                }
                CurrentPositionBtn(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .align(Alignment.BottomStart),
                    onClick = vm::onClickCurrentPositionBtn,
                )
                FoodSpotReportButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = vm::onClickReportBtn,
                )
            }
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
                    Modifier.clickable {
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

private fun drawMarkers(
    map: KakaoMap,
    foodSpots: List<FoodSpotState>,
    isRefresh: Boolean = true,
) {
    if (isRefresh) {
        map.labelManager?.layer?.removeAll()
    }
    foodSpots.forEach {
        val styles =
            map.labelManager
                ?.addLabelStyles(LabelStyles.from(LabelStyle.from(android.R.drawable.star_on)))
        val options =
            LabelOptions
                .from(it.position)
                .setStyles(styles)
                .apply {
                    labelId = it.id.toString()
                }
        options.labelId = it.id.toString()
        map.labelManager?.layer?.addLabel(options)
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
