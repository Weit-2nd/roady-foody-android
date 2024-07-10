package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.CoordinateDTO

class CoordinateType : NavType<CoordinateDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): CoordinateDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, CoordinateDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): CoordinateDTO {
        return Gson().fromJson(value, CoordinateDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: CoordinateDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
