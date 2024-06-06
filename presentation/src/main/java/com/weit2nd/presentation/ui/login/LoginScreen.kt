package com.weit2nd.presentation.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    navToHome: (User) -> Unit,
    navToSignUp: (User) -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.NavToHome -> {
                navToHome(sideEffect.user)
            }

            is LoginSideEffect.NavToSignUp -> {
                navToSignUp(sideEffect.user)
            }
        }
    }

    Surface {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            LoginButton(
                modifier = Modifier.padding(8.dp),
                onClick = vm::onLoginButtonClick,
                enabled = state.value.isLoading.not(),
            )
        }
    }
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(
            text = stringResource(id = R.string.login_btn),
        )
    }
}
