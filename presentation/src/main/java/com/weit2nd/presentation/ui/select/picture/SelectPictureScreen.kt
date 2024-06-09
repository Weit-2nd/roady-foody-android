package com.weit2nd.presentation.ui.select.picture

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.gun0912.tedpermission.coroutine.TedPermission
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectPictureScreen(
    vm: SelectPictureViewModel = hiltViewModel(),
) {
    val state = vm.collectAsState()
    LaunchedEffect(Unit) {
        vm.onCreate()
    }
    LaunchedEffect(state.value.images) {
        val images = state.value.images
        Log.d("MainTest", "${images.size} ${images.lastOrNull()}")
    }
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "이미지 선택 화면")
        }
        it
    }
}
