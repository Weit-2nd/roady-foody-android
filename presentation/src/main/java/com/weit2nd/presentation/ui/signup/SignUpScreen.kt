package com.weit2nd.presentation.ui.signup

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.NicknameState
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
            ProfileImageContainer(
                imgUri = state.value.profileImageUri,
                onProfileImageClick = vm::onProfileImageClick
            )
            Spacer(modifier = Modifier.padding(16.dp))
            NicknameSetting(
                nickname = state.value.nickname,
                nicknameState = state.value.nicknameState,
                isLoading = state.value.isLoading,
                onInputValueChange = vm::onNicknameInputValueChange,
                onDuplicationBtnClick = vm::onDuplicationBtnClick,
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = vm::onSignUpButtonClick,
            enabled = state.value.nicknameState == NicknameState.CAN_SIGN_UP
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
fun ProfileImageContainer(
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

    ProfileImage(
        modifier = modifier,
        painter = painter,
        storageAccessLauncher = storageAccessLauncher,
    )
}

@Composable
private fun ProfileImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    storageAccessLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
) {
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
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun NicknameSetting(
    modifier: Modifier = Modifier,
    nickname: String,
    nicknameState: NicknameState,
    isLoading: Boolean,
    onInputValueChange: (String) -> Unit,
    onDuplicationBtnClick: (String) -> Unit,
) {
    NicknameContainer(
        nickname = nickname,
        onInputValueChange = onInputValueChange,
        isNicknameValid = (nicknameState == NicknameState.VALID) && !isLoading,
        onDuplicationBtnClick = onDuplicationBtnClick
    )
    Text(
        modifier = modifier.padding(8.dp),
        textAlign = TextAlign.Center,
        color = if (nicknameState == NicknameState.CAN_SIGN_UP) {
            Color.Blue
        } else {
            Color.Red
        },
        text = when (nicknameState) {
            NicknameState.INVALID_LENGTH -> stringResource(R.string.nickname_invalid_length)
            NicknameState.INVALID_CHARACTERS -> stringResource(R.string.nickname_invalid_character)
            NicknameState.INVALID_CONTAIN_SPACE -> stringResource(R.string.nickname_invalid_space)
            NicknameState.DUPLICATE -> stringResource(R.string.nickname_duplicate)
            NicknameState.CAN_SIGN_UP -> stringResource(R.string.nickname_can_sign_up)
            else -> ""
        }
    )
}

@Composable
fun NicknameContainer(
    modifier: Modifier = Modifier,
    nickname: String,
    onInputValueChange: (String) -> Unit,
    isNicknameValid: Boolean,
    onDuplicationBtnClick: (String) -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue(nickname)) }

    NicknameTextField(
        userInput = userInput,
        onInputValueChange = { newValue ->
            userInput = newValue
            onInputValueChange(newValue.text)
        },
        onDuplicationBtnClick = onDuplicationBtnClick,
        isNicknameValid = isNicknameValid,
        modifier = modifier,
    )
}

@Composable
private fun NicknameTextField(
    userInput: TextFieldValue,
    onInputValueChange: (TextFieldValue) -> Unit,
    onDuplicationBtnClick: (String) -> Unit,
    isNicknameValid: Boolean,
    modifier: Modifier
) {
    TextField(
        value = userInput,
        onValueChange = { newValue ->
            onInputValueChange(newValue)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text(stringResource(R.string.nickname_input_placeholder))
        },
        trailingIcon = {
            DuplicationCheckButton(
                onClick = onDuplicationBtnClick,
                userInput = userInput,
                enable = isNicknameValid,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        singleLine = true
    )
}

@Composable
private fun DuplicationCheckButton(
    onClick: (String) -> Unit,
    userInput: TextFieldValue,
    enable: Boolean
) {
    Button(
        onClick = { onClick(userInput.text) },
        enabled = enable
    ) {
        Text(text = stringResource(R.string.nickname_duplicate_check))
    }
}
