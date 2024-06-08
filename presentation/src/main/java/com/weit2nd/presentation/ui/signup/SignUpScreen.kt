package com.weit2nd.presentation.ui.signup

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = hiltViewModel(),
    navToHome: (User) -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        handleSideEffects(sideEffect, navToHome)
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
                imgUri = state.value.profileImageUri,
                onProfileImageClick = vm::onProfileImageClick
            )
            Spacer(modifier = Modifier.padding(16.dp))
            NicknameSetting(state, vm)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = vm::onSignUpButtonClick,
            enabled = state.value.canSignUp
        ) {
            Text(text = stringResource(R.string.sign_up))
        }
    }
}

private fun handleSideEffects(
    sideEffect: SignUpSideEffect,
    navToHome: (User) -> Unit
) {
    when (sideEffect) {
        is SignUpSideEffect.NavToHome -> {
            navToHome(sideEffect.user)
        }
    }
}

@Composable
private fun NicknameSetting(
    state: State<SignUpState>,
    vm: SignUpViewModel
) {
    NicknameInput(
        nickname = state.value.nickname,
        onInputValueChange = vm::onInputValueChange,
        isNicknameValid = state.value.isNicknameValid,
        onDuplicationBtnClick = vm::onDuplicationBtnClick
    )
    Text(
        modifier = Modifier.padding(8.dp),
        textAlign = TextAlign.Center,
        color = Color.Red,
        text = when (state.value.warningState) {
            WarningState.IS_VALID -> ""
            WarningState.IS_NOT_VALID -> stringResource(R.string.nickname_warning_not_valid)
            WarningState.IS_DUPLICATE -> stringResource(R.string.nickname_warning_duplicate)
        }
    )
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imgUri: Uri? = null,
    onProfileImageClick: (Uri?) -> Unit,
) {
    var imageUri by remember { mutableStateOf(imgUri) }

    val context = LocalContext.current
    val storageAccessLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                onProfileImageClick(imageUri)
            }
        }

    val painter = if (imageUri != null) {
        val bitmap = ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                imageUri!!
            )
        )
        BitmapPainter(bitmap.asImageBitmap())
    } else {
        // 기본 프로필 이미지
        painterResource(R.drawable.ic_launcher_background)
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .size(200.dp)
            .clickable {
                storageAccessLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun NicknameInput(
    modifier: Modifier = Modifier,
    nickname: String,
    onInputValueChange: (String) -> Unit,
    isNicknameValid: Boolean,
    onDuplicationBtnClick: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue(nickname)) }

    TextField(
        value = userInput,
        onValueChange = { newValue ->
            if (userInput.text != newValue.text){
                userInput = newValue
                onInputValueChange(userInput.text)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text(stringResource(R.string.nickname_input_placeholder))
        },
        trailingIcon = {
            DuplicationCheckButton(onDuplicationBtnClick, userInput, isNicknameValid)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        singleLine = true
    )
}

@Composable
private fun DuplicationCheckButton(
    onDuplicationBtnClick: (String) -> Unit,
    userInput: TextFieldValue,
    isNicknameValid: Boolean
) {
    Button(
        onClick = { onDuplicationBtnClick(userInput.text) },
        enabled = isNicknameValid
    ) {
        Text(text = stringResource(R.string.nickname_duplicate_check))
    }
}
