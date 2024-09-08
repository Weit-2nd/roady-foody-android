package com.weit2nd.presentation.ui.select.place.map

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.weit2nd.domain.model.search.Place
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.common.currentposition.CurrentPositionBtn
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectPlaceMapScreen(
    vm: SelectPlaceMapViewModel = hiltViewModel(),
    onSelectPlace: (Place) -> Unit,
    navToBack: () -> Unit,
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(context, sideEffect, onSelectPlace, navToBack)
    }
    val mapView =
        remember {
            MapView(context).apply {
                start(
                    mapLifeCycleCallback(),
                    kakaoMapReadyCallback(
                        vm::onMapReady,
                        onCameraMoveStart = vm::onCameraMoveStart,
                        onCameraMoveEnd = vm::onCameraMoveEnd,
                        position =
                            state.value.initialPosition.run {
                                LatLng.from(
                                    latitude,
                                    longitude,
                                )
                            },
                    ),
                )
            }
        }

    DisposableEffectWithLifeCycle(onResume = mapView::resume, onPause = mapView::pause)

    var mapRectSize by remember { mutableStateOf(IntSize.Zero) }

    Scaffold(
        topBar = {
            BackTopBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.select_place_map_toolbar_title),
                onClickBackBtn = vm::onClickBackBtn,
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .onGloballyPositioned { layoutCoordinates ->
                                mapRectSize = layoutCoordinates.size
                            },
                    contentAlignment = Alignment.TopStart,
                ) {
                    AndroidView(
                        modifier = Modifier,
                        factory = { mapView },
                    )

                    CurrentPositionBtn(
                        modifier =
                            Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 32.dp)
                                .size(40.dp),
                        onClick = vm::onClickCurrentPositionBtn,
                    )

                    var selectMarkerIconSize by remember { mutableStateOf(IntSize.Zero) }
                    PositionSelectMarker(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .onGloballyPositioned { layoutCoordinates ->
                                    selectMarkerIconSize = layoutCoordinates.size
                                    val centerX = mapRectSize.width / 2
                                    val centerY = mapRectSize.height / 2
                                    vm.onGloballyPositioned(IntOffset(centerX, centerY))
                                }.offset {
                                    IntOffset(
                                        state.value.selectMarkerOffset.x - selectMarkerIconSize.width / 2,
                                        state.value.selectMarkerOffset.y - selectMarkerIconSize.height / 2,
                                    )
                                },
                    )
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                ),
                    )
                }

                PlaceInfoView(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(128.dp)
                            .padding(horizontal = 16.dp),
                    canSelect = state.value.isLoading.not() && state.value.isAvailablePlace,
                    place = state.value.place,
                    onClick = vm::onClickSelectPlaceBtn,
                )
            }
        },
    )
}

private fun handleSideEffects(
    context: Context,
    sideEffect: SelectPlaceMapSideEffect,
    onSelectPlace: (Place) -> Unit,
    navToBack: () -> Unit,
) {
    when (sideEffect) {
        is SelectPlaceMapSideEffect.MoveCamera -> {
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(sideEffect.position)
            sideEffect.map.moveCamera(cameraUpdate)
        }

        is SelectPlaceMapSideEffect.SelectPlace -> {
            onSelectPlace(sideEffect.place)
        }

        is SelectPlaceMapSideEffect.ShowToast -> {
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }

        SelectPlaceMapSideEffect.NavToBack -> {
            navToBack()
        }
    }
}

@Composable
private fun PositionSelectMarker(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_my_location),
        tint = MaterialTheme.colorScheme.tertiary,
        contentDescription = "positionSelectMarker",
    )
}

@Composable
private fun PlaceInfoView(
    modifier: Modifier = Modifier,
    canSelect: Boolean,
    place: Place,
    onClick: () -> Unit,
) {
    Column(
        modifier =
        modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = place.addressName,
            style = MaterialTheme.typography.headlineSmall,
            color = Black,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = place.roadAddressName,
            style = MaterialTheme.typography.titleMedium,
            color = Black,
        )

        BottomButton(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            onClick = { onClick() },
            enabled = canSelect,
            text = stringResource(R.string.select_place_map_register_btn),
            textStyle = MaterialTheme.typography.titleSmall,
        )
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

private fun kakaoMapReadyCallback(
    onMapReady: (KakaoMap) -> Unit,
    onCameraMoveStart: () -> Unit,
    onCameraMoveEnd: () -> Unit,
    position: LatLng,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady(map)
        onCameraMoveEnd()
        map.setOnCameraMoveStartListener { _, _ ->
            onCameraMoveStart()
        }
        map.setOnCameraMoveEndListener { kakaoMap, _, _ ->
            onCameraMoveEnd()
        }
    }

    override fun getPosition(): LatLng = position
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

@Preview(showBackground = true)
@Composable
private fun PlaceInfoViewPreview() {
    RoadyFoodyTheme {
        PlaceInfoView(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(horizontal = 16.dp),
            canSelect = true,
            place =
                Place(
                    "김포국제공항 국내선",
                    "서울 강서구 공항동 1373",
                    "서울 강서구 하늘길 77",
                    0.0,
                    0.0,
                    "",
                ),
        ) {
//
        }
    }
}
