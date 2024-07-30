package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.ImageViewerDTO

class ImageViewerDataType :
    BaseNavType<ImageViewerDTO>(
        target = ImageViewerDTO::class.java,
        isNullableAllowed = false,
    )
