package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDTO(
    val name: String,
) : Parcelable

fun UserDTO.toUser(): User =
    User(
        name = name,
    )

fun User.toUserDTO(): UserDTO =
    UserDTO(
        name = name,
    )
