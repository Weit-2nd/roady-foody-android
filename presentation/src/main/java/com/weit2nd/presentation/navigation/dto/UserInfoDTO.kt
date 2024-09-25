package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoDTO(
    val userId: Long,
    val nickname: String,
    val profileImage: String?,
) : Parcelable
