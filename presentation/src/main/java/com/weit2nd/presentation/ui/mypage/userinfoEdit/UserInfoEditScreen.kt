package com.weit2nd.presentation.ui.mypage.userinfoEdit

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
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.LoadingDialogScreen
import com.weit2nd.presentation.ui.common.ProfileSettingScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun UserInfoEditScreen(
    vm: UserInfoEditViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state = vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            UserInfoEditSideEffect.NavToBack -> {
                navToBack()
            }

            is UserInfoEditSideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val canSignUp by remember {
        derivedStateOf {
            state.value.nicknameState == NicknameState.CAN_SIGN_UP &&
                state.value.isLoading.not()
        }
    }

    val isNicknameValid by remember {
        derivedStateOf {
            (state.value.nicknameState == NicknameState.VALID) && !state.value.isNicknameCheckingLoading
        }
    }

    Box {
        if (state.value.isLoading) {
            LoadingDialogScreen()
        }

        Scaffold(
            topBar = {
                BackTopBar(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                    title = "프로필 수정",
                    onClickBackBtn = vm::onBackButtonClick,
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
                    onSignUpButtonClick = vm::onEditButtonClick,
                    canSignUp = canSignUp,
                    signUpButtonTitle = stringResource(R.string.edit_profile_sign_up),
                )
            },
        )
    }
}
