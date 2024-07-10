package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.UserDTO

class UserType : NavType<UserDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): UserDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, UserDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): UserDTO {
        return Gson().fromJson(value, UserDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: UserDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
