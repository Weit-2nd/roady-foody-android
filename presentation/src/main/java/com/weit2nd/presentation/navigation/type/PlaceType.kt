package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.PlaceDTO

class PlaceType : NavType<PlaceDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): PlaceDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, PlaceDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): PlaceDTO {
        return Gson().fromJson(value, PlaceDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: PlaceDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
