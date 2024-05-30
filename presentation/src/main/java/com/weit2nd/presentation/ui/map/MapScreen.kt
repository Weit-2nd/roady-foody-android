package com.weit2nd.presentation.ui.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    position: LatLng = LatLng.from(37.566, 126.978),
    onMapReady: () -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            start(
                mapLifeCycleCallback(),
                kakaoMapReadyCallback(onMapReady, position),
            )
        }
    }

    CompositionLocalProvider(
        LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
    ) {
        DisposableEffectWithLifeCycle(onResume = mapView::resume, onPause = mapView::pause)
    }

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
    position: LatLng
) = object : KakaoMapReadyCallback() {
    override fun onMapReady(map: KakaoMap) {
        onMapReady()
    }

    override fun getPosition(): LatLng = position
}

@Composable
private fun DisposableEffectWithLifeCycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
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
