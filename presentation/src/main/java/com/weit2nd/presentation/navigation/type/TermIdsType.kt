package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.TermIdsDTO

class TermIdsType : NavType<TermIdsDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): TermIdsDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, TermIdsDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): TermIdsDTO {
        return Gson().fromJson(value, TermIdsDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: TermIdsDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
