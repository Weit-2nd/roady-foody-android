package com.weit2nd.presentation.ui.splash

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SplashScreen(
    vm: SplashViewModel = hiltViewModel(),
    navToLogin: () -> Unit,
    navToHome: () -> Unit,
) {
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SplashSideEffect.NavToHome -> navToHome()
            SplashSideEffect.NavToLogin -> navToLogin()
            is SplashSideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val alpha =
        remember {
            Animatable(0f)
        }
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500),
        )
        vm.onCreate()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .alpha(alpha.value),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Splash",
        )
    }
}
