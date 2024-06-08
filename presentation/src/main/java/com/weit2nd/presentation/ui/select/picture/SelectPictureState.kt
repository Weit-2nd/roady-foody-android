package com.weit2nd.presentation.ui.select.picture

import com.weit2nd.domain.model.localimage.LocalImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectPictureState(
    val images: ImmutableList<LocalImage> = persistentListOf(),
)
