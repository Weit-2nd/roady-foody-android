package com.weit2nd.presentation.ui.select.location.map

import android.R
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.weit2nd.presentation.ui.common.currentposition.CurrentPositionBtn
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectLocationMapScreen(
    vm: SelectLocationMapViewModel = hiltViewModel(),
    position: LatLng = LatLng.from(37.5597706, 126.9423666),
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SelectLocationMapSideEffect.MoveCamera -> {
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(sideEffect.position)
                sideEffect.map.moveCamera(cameraUpdate)
            }
        }
    }
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

    DisposableEffectWithLifeCycle(onResume = mapView::resume, onPause = mapView::pause)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(3f)) {
            AndroidView(
                modifier = Modifier,
                factory = { mapView }
            )

            CurrentPositionBtn(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 24.dp),
                onClick = vm::onClickCurrentPositionBtn
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.value.location.locationDetail,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { }
            ) {
                Text(text = "이 위치로 등록")
            }
        }

    }
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
    onCameraMoveEnd: (LatLng?) -> Unit,
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
    onCameraMoveEnd: (LatLng?) -> Unit,
) {
    val viewport = map.viewport

    val x = viewport.width() / 2
    val y = viewport.height() / 2
    val position = map.fromScreenPoint(x, y)
    onCameraMoveEnd(position)

    map.labelManager?.layer?.removeAll()
    val styles = map.labelManager
        ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_menu_mylocation)))
    val options = LabelOptions.from(position).setStyles(styles)
    map.labelManager?.layer?.addLabel(options)

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

