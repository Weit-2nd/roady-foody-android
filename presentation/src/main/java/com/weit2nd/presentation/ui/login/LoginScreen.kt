package com.weit2nd.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.KakaoLoginBackground
import com.weit2nd.presentation.ui.theme.KakaoLoginText
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    navToHome: () -> Unit,
    navToTermAgreement: () -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.NavToHome -> {
                navToHome()
            }

            is LoginSideEffect.NavToTermAgreement -> {
                navToTermAgreement()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LoginIconScreen(
            modifier =
                Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .offset(y = (-24).dp)
                    .align(Alignment.Center),
        )
        LoginButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp)
                    .offset(y = (-72).dp),
            onClick = vm::onLoginButtonClick,
            enabled = state.value.isLoading.not(),
        )
    }
}

@Composable
private fun LoginIconScreen(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "",
    )
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(KakaoLoginBackground)
                .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_kakao), contentDescription = "")
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.login_btn),
            style = MaterialTheme.typography.headlineSmall,
            color = KakaoLoginText,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginButtonPreview() {
    RoadyFoodyTheme {
        LoginButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            onClick = { },
            enabled = true,
        )
    }
}
