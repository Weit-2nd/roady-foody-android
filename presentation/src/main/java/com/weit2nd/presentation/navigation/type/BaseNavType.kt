package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson

abstract class BaseNavType<T : Parcelable>(
    private val target: Class<T>,
    override val isNullableAllowed: Boolean = false,
) : NavType<T?>(isNullableAllowed = isNullableAllowed) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, target)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): T? {
        return Gson().fromJson(value, target)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: T?,
    ) {
        bundle.putParcelable(key, value)
    }
}
