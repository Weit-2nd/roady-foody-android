package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.presentation.ui.common.ImageViewerData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageViewerDTO(
    val images: List<String>,
    val position: Int,
) : Parcelable

fun ImageViewerDTO.toImageViewerData(): ImageViewerData = ImageViewerData(
    images = images, position = position
)

fun ImageViewerData.toImageViewerDTO(): ImageViewerDTO = ImageViewerDTO(
    images = images, position = position
)
