package com.weit2nd.presentation.ui.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.User
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = hiltViewModel(),
    navToHome: (User) -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SignUpSideEffect.NavToHome -> {
                navToHome(sideEffect.user)
            }
        }
    }

    Column {
        Button(onClick = vm::onSignUpButtonClick) {
            Text(text = "회원가입")
        }
    }
}

@Composable
fun ProfilePicture() {

}

@Composable
fun NicknameInput(
    modifier: Modifier = Modifier
) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = userInput,
        onValueChange = { newValue ->
            userInput = newValue
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text("닉네임")
        },
        modifier = modifier
            .fillMaxWidth(),
        singleLine = true
    )
}
