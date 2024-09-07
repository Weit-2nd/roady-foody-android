package com.weit2nd.presentation.ui.splash

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
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

    SplashIconScreen(
        modifier =
            Modifier
                .fillMaxSize()
                .alpha(alpha.value),
    )
}

@Composable
private fun SplashIconScreen(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .offset(y = (-24).dp),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SplashIconScreenPreview() {
    RoadyFoodyTheme {
        SplashIconScreen(modifier = Modifier.fillMaxSize())
    }
}
