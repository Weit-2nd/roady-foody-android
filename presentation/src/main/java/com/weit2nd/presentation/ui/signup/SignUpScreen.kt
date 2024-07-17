package com.weit2nd.presentation.ui.signup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.LoadingDialogScreen
import com.weit2nd.presentation.ui.common.ProfileImage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = hiltViewModel(),
    navToHome: (User) -> Unit,
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

    if (state.value.isSignUpLoading) {
        LoadingDialogScreen()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(24.dp))
            ProfileImage(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .size(200.dp),
                imgUri = state.value.profileImageUri,
                onProfileImageClick = vm::onProfileImageClick,
            )
            Spacer(modifier = Modifier.padding(16.dp))
            NicknameSetting(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                nickname = state.value.nickname,
                nicknameState = state.value.nicknameState,
                isLoading = state.value.isNicknameCheckingLoading,
                onInputValueChange = vm::onNicknameInputValueChange,
                onDuplicationBtnClick = vm::onDuplicationBtnClick,
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = vm::onSignUpButtonClick,
            enabled = state.value.nicknameState == NicknameState.CAN_SIGN_UP,
        ) {
            Text(text = stringResource(R.string.sign_up))
        }
    }
}

private fun handleSideEffects(
    sideEffect: SignUpSideEffect,
    navToHome: (User) -> Unit,
    context: Context,
) {
    when (sideEffect) {
        is SignUpSideEffect.NavToHome -> {
            navToHome(sideEffect.user)
        }

        is SignUpSideEffect.ShowToast -> {
            Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun NicknameSetting(
    modifier: Modifier = Modifier,
    nickname: String,
    nicknameState: NicknameState,
    isLoading: Boolean,
    onInputValueChange: (String) -> Unit,
    onDuplicationBtnClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NicknameTextField(
            modifier = modifier,
            nickname = nickname,
            onInputValueChange = onInputValueChange,
            isNicknameValid = (nicknameState == NicknameState.VALID) && !isLoading,
            onDuplicationBtnClick = onDuplicationBtnClick,
        )

        Text(
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center,
            color =
                if (nicknameState == NicknameState.CAN_SIGN_UP) {
                    Color.Blue
                } else {
                    Color.Red
                },
            text = getNicknameStateMessage(nicknameState),
        )
    }
}

@Composable
private fun getNicknameStateMessage(nicknameState: NicknameState) =
    when (nicknameState) {
        NicknameState.INVALID_LENGTH -> stringResource(R.string.nickname_invalid_length)
        NicknameState.INVALID_CHARACTERS -> stringResource(R.string.nickname_invalid_character)
        NicknameState.INVALID_CONTAIN_SPACE -> stringResource(R.string.nickname_invalid_space)
        NicknameState.DUPLICATE -> stringResource(R.string.nickname_duplicate)
        NicknameState.CAN_SIGN_UP -> stringResource(R.string.nickname_can_sign_up)
        else -> ""
    }

@Composable
fun NicknameTextField(
    modifier: Modifier = Modifier,
    nickname: String,
    onInputValueChange: (String) -> Unit,
    isNicknameValid: Boolean,
    onDuplicationBtnClick: () -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue(nickname)) }

    TextField(
        value = userInput,
        onValueChange = { newValue ->
            userInput = newValue
            onInputValueChange(newValue.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text(stringResource(R.string.nickname_input_placeholder))
        },
        trailingIcon = {
            DuplicationCheckButton(
                onClick = onDuplicationBtnClick,
                enable = isNicknameValid,
            )
        },
        modifier = modifier,
        singleLine = true,
    )
}

@Composable
private fun DuplicationCheckButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enable: Boolean,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
    ) {
        Text(text = stringResource(R.string.nickname_duplicate_check))
    }
}
