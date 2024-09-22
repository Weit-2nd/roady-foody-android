package com.weit2nd.presentation.ui.common

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weit2nd.domain.model.NicknameState
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Blue
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Gray3
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme


@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    profileImage: Uri?,
    onProfileImageClick: () -> Unit,
    nickname: String,
    nicknameState: NicknameState,
    onNicknameInputValueChange: (String) -> Unit,
    onDuplicationBtnClick: () -> Unit,
    isNicknameValid: Boolean,
    onSignUpButtonClick: () -> Unit,
    canSignUp: Boolean,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.sign_up_profile_image),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            EditableProfileImage(
                modifier =
                Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                imgUri = profileImage,
                onProfileImageClick = onProfileImageClick,
            )
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = stringResource(R.string.sign_up_nickname),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            NicknameSetting(
                modifier =
                Modifier
                    .fillMaxWidth(),
                nickname = nickname,
                nicknameState = nicknameState,
                onInputValueChange = onNicknameInputValueChange,
                onDuplicationBtnClick = onDuplicationBtnClick,
                isNicknameValid = isNicknameValid,
            )
        }

        BottomButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignUpButtonClick,
            enabled = canSignUp,
            text = stringResource(R.string.sign_up),
        )
    }
}

@Composable
private fun NicknameSetting(
    modifier: Modifier = Modifier,
    nickname: String,
    nicknameState: NicknameState,
    onInputValueChange: (String) -> Unit,
    onDuplicationBtnClick: () -> Unit,
    isNicknameValid: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NicknameTextField(
            modifier = Modifier.fillMaxWidth(),
            nickname = nickname,
            onInputValueChange = onInputValueChange,
            isNicknameValid = isNicknameValid,
            onDuplicationBtnClick = onDuplicationBtnClick,
        )

        Text(
            modifier =
            Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            color =
            when (nicknameState) {
                NicknameState.CAN_SIGN_UP -> Blue
                NicknameState.EMPTY, NicknameState.VALID -> Gray2
                else -> MaterialTheme.colorScheme.error
            },
            text = getNicknameStateMessage(nicknameState),
            style = MaterialTheme.typography.bodyMedium,
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
        else -> stringResource(R.string.nickname_description)
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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = userInput,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            placeholder = {
                Text(
                    text = stringResource(R.string.nickname_input_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Gray2,
                )
            },
            colors =
            TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Gray3,
                disabledIndicatorColor = Gray3,
                focusedIndicatorColor = Gray3,
            ),
            onValueChange = { newValue ->
                userInput = newValue
                onInputValueChange(newValue.text)
            },
            singleLine = true,
        )
        DuplicationCheckButton(
            modifier =
            Modifier
                .height(48.dp)
                .padding(start = 12.dp),
            onClick = onDuplicationBtnClick,
            enable = isNicknameValid,
        )
    }
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
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = stringResource(R.string.nickname_duplicate_check),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DuplicationButtonPreview() {
    RoadyFoodyTheme {
        DuplicationCheckButton(
            modifier = Modifier.height(48.dp),
            onClick = {},
            enable = true,
        )
    }
}
