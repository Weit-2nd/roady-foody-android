package com.weit2nd.presentation.ui.signup

import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
        when (sideEffect) {
            is SignUpSideEffect.NavToHome -> {
                navToHome(sideEffect.user)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ProfileImage(
            imgUri = state.value.profileImageUri,
            onProfileImageClick = vm::onProfileImageClick
        )
        Button(onClick = vm::onSignUpButtonClick) {
            Text(text = "회원가입")
        }
    }
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
