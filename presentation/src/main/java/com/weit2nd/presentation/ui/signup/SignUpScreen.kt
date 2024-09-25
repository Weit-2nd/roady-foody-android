package com.weit2nd.presentation.ui.signup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.LoadingDialogScreen
import com.weit2nd.presentation.ui.common.ProfileSettingScreen
import com.weit2nd.presentation.ui.common.TitleTopBar
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = hiltViewModel(),
    navToHome: () -> Unit,
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(
            sideEffect = sideEffect,
            navToHome = navToHome,
            context = context,
        )
    }

    val canSignUp by remember {
        derivedStateOf {
            state.value.nicknameState == NicknameState.CAN_SET_PROFILE &&
                state.value.isSignUpLoading.not()
        }
    }

    val isNicknameValid by remember {
        derivedStateOf {
            (state.value.nicknameState == NicknameState.VALID) && !state.value.isNicknameCheckingLoading
        }
    }

    Box {
        if (state.value.isSignUpLoading) {
            LoadingDialogScreen()
        }

        Scaffold(
            topBar = {
                TitleTopBar(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                    title = stringResource(R.string.term_screen_topbar_title),
                )
            },
            content = { innerPadding ->
                ProfileSettingScreen(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                    profileImage = state.value.profileImageUri,
                    onProfileImageClick = vm::onProfileImageClick,
                    nickname = state.value.nickname,
                    nicknameState = state.value.nicknameState,
                    onNicknameInputValueChange = vm::onNicknameInputValueChange,
                    onDuplicationBtnClick = vm::onDuplicationBtnClick,
                    isNicknameValid = isNicknameValid,
                    onSetProfileButtonClick = vm::onSignUpButtonClick,
                    canSetProfile = canSignUp,
                    setProfileButtonTitle = stringResource(R.string.sign_up),
                )
            },
        )
    }
}

private fun handleSideEffects(
    sideEffect: SignUpSideEffect,
    navToHome: () -> Unit,
    context: Context,
) {
    when (sideEffect) {
        is SignUpSideEffect.NavToHome -> {
            navToHome()
        }

        is SignUpSideEffect.ShowToast -> {
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }
}
