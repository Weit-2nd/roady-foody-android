package com.weit2nd.presentation.ui.map

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    vm: MapViewModel = hiltViewModel(),
    position: LatLng = LatLng.from(37.566, 126.978),
    onMapReady: () -> Unit = {}
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            start(
                mapLifeCycleCallback(),
                kakaoMapReadyCallback(onMapReady, position, state.value.restaurants),
            )
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
    onMapReady: () -> Unit,
    position: LatLng,
    restaurants: List<RestaurantState>,
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady()
        // todo 음식점 마커 띄우기
    }

    override fun getPosition(): LatLng = position
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
