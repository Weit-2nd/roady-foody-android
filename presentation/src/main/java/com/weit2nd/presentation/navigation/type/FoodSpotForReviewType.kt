package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO

class FoodSpotForReviewType : NavType<FoodSpotForReviewDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): FoodSpotForReviewDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, FoodSpotForReviewDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): FoodSpotForReviewDTO {
        return Gson().fromJson(value, FoodSpotForReviewDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: FoodSpotForReviewDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
