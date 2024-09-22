package com.weit2nd.presentation.ui.mypage.userinfoEdit

import android.net.Uri
import com.weit2nd.domain.model.NicknameState

data class UserInfoEditState(
    val isLoading: Boolean = false,
    val profileImageUri: Uri? = null,
    val nickname: String = "",
    val isNicknameCheckingLoading: Boolean = false,
    val nicknameState: NicknameState = NicknameState.EMPTY,
)
