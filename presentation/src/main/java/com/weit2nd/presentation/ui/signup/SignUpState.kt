package com.weit2nd.presentation.ui.signup

import android.net.Uri
import com.weit2nd.domain.model.NicknameState

data class SignUpState(
    val profileImageUri: Uri? = null,
    val nickname: String = "",
    val isNicknameCheckingLoading: Boolean = false,
    val nicknameState: NicknameState = NicknameState.EMPTY,
    val isSignUpLoading: Boolean = false,
)
