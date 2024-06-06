package com.weit2nd.presentation.ui.map

import android.R
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    vm: MapViewModel = hiltViewModel(),
    position: LatLng = LatLng.from(37.5597706, 126.9423666),
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            start(
                mapLifeCycleCallback(),
                kakaoMapReadyCallback(
                    vm::onMapReady,
                    vm::onCameraMoveEnd,
                    position,
                ),
            )
        }
    }

    LaunchedEffect(state.value.restaurants) {
        state.value.map?.let {
            drawMarkers(it, state.value.restaurants)
        }
    }

    DisposableEffectWithLifeCycle(onResume = mapView::resume, onPause = mapView::pause)

    AndroidView(
        modifier = modifier,
        factory = { mapView }
    )
}

private fun mapLifeCycleCallback() = object : MapLifeCycleCallback() {
    override fun onMapDestroy() {
        Log.d("KakaoMap", "onMapDestroy")
    }

    override fun onMapError(error: Exception) {
        Log.e("KakaoMap", "onMapError: ", error)
    }
}

private fun kakaoMapReadyCallback(
    onMapReady: (KakaoMap) -> Unit,
    onCameraMoveEnd: (startLat: Double, startLng: Double, endLat: Double, endLng: Double) -> Unit,
    position: LatLng,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady(map)
        onCameraMoveEnd(map, onCameraMoveEnd)
        map.setOnCameraMoveEndListener { kakaoMap, _, _ ->
            onCameraMoveEnd(kakaoMap, onCameraMoveEnd)
        }
    }

    override fun getPosition(): LatLng = position
}

private fun onCameraMoveEnd(
    map: KakaoMap,
    onCameraMoveEnd: (startLat: Double, startLng: Double, endLat: Double, endLng: Double) -> Unit,
) {
    val viewport = map.viewport
    val startCoordinate = map.fromScreenPoint(0, 0)
    val endCoordinate = map.fromScreenPoint(viewport.width(), viewport.height())
    if (startCoordinate != null && endCoordinate != null) {
        onCameraMoveEnd(
            startCoordinate.getLatitude(),
            startCoordinate.getLongitude(),
            endCoordinate.latitude,
            endCoordinate.longitude,
        )
    }
}

private fun drawMarkers(
    map: KakaoMap,
    restaurants: List<RestaurantState>,
    isRefresh: Boolean = true,
) {
    if (isRefresh) {
        map.labelManager?.layer?.removeAll()
    }
    restaurants.forEach {
        val styles = map.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.star_on)))
        val options = LabelOptions.from(it.position).setStyles(styles)
        map.labelManager?.layer?.addLabel(options)
    }
}

@Composable
private fun DisposableEffectWithLifeCycle(
    // 오류로 compose.ui의 LocalLifecycleOwner 사용
    lifecycleOwner: LifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    onResume: () -> Unit,
    onPause: () -> Unit
) {
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
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
