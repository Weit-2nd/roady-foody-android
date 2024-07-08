package com.weit2nd.presentation.ui.signup

import android.net.Uri
import com.weit2nd.domain.model.NicknameState

data class SignUpState(
    val agreedTermIds: List<Long> = emptyList(),
    val profileImageUri: Uri? = null,
    val nickname: String = "",
    val isLoading: Boolean = false,
    val nicknameState: NicknameState = NicknameState.EMPTY,
)
