package com.weit2nd.presentation.navigation.type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.weit2nd.presentation.navigation.dto.ImageViewerDTO

class ImageViewerDataType : NavType<ImageViewerDTO>(isNullableAllowed = false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): ImageViewerDTO? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, ImageViewerDTO::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): ImageViewerDTO {
        return Gson().fromJson(value, ImageViewerDTO::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: ImageViewerDTO,
    ) {
        bundle.putParcelable(key, value)
    }
}
