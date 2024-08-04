package com.weit2nd.presentation.ui.select.place.map

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.weit2nd.presentation.ui.common.currentposition.CurrentPositionBtn
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectPlaceMapScreen(
    vm: SelectLocationMapViewModel = hiltViewModel(),
    onSelectPlace: (Place) -> Unit,
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(context, sideEffect, onSelectPlace)
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
                        selectMarkerOffset = state.value.selectMarkerOffset,
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
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .weight(3f)
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
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 24.dp),
                onClick = vm::onClickCurrentPositionBtn,
            )

            PositionSelectMarker(
                modifier =
                    Modifier
                        .onGloballyPositioned { layoutCoordinates ->
                            val imageSize = layoutCoordinates.size
                            val centerX = mapRectSize.width / 2 - imageSize.width / 2
                            val centerY = mapRectSize.height / 2 - imageSize.height / 2
                            vm.onGloballyPositioned(IntOffset(centerX, centerY))
                        }.offset { state.value.selectMarkerOffset },
            )
        }
        PlaceInfoView(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(16.dp),
            canSelect = state.value.isLoading.not() && state.value.isAvailablePlace,
            place = state.value.place,
            onClick = vm::onClickSelectPlaceBtn,
        )
    }
}

private fun handleSideEffects(
    context: Context,
    sideEffect: SelectLocationMapSideEffect,
    onSelectPlace: (Place) -> Unit,
) {
    when (sideEffect) {
        is SelectLocationMapSideEffect.MoveCamera -> {
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(sideEffect.position)
            sideEffect.map.moveCamera(cameraUpdate)
        }

        is SelectLocationMapSideEffect.SelectPlace -> {
            onSelectPlace(sideEffect.place)
        }

        is SelectLocationMapSideEffect.ShowToast -> {
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun PositionSelectMarker(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(android.R.drawable.ic_menu_mylocation),
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
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = place.addressName,
            style =
                TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ),
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = place.roadAddressName,
            style = TextStyle(fontSize = 16.sp),
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() },
            enabled = canSelect,
        ) {
            Text(text = "이 위치로 등록")
        }
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
    onCameraMoveEnd: (LatLng?) -> Unit,
    selectMarkerOffset: IntOffset,
    position: LatLng,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady(map)
        onCameraMoveEnd(map, onCameraMoveEnd, selectMarkerOffset)
        map.setOnCameraMoveStartListener { _, _ ->
            onCameraMoveStart()
        }
        map.setOnCameraMoveEndListener { kakaoMap, _, _ ->
            onCameraMoveEnd(kakaoMap, onCameraMoveEnd, selectMarkerOffset)
        }
    }

    override fun getPosition(): LatLng = position
}

private fun onCameraMoveEnd(
    map: KakaoMap,
    onCameraMoveEnd: (LatLng?) -> Unit,
    selectMarkerOffset: IntOffset,
) {
    val position = map.fromScreenPoint(selectMarkerOffset.x, selectMarkerOffset.y)
    onCameraMoveEnd(position)
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
