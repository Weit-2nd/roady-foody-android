package com.weit2nd.data.model

import com.weit2nd.domain.model.localimage.LocalImage

/**
 * @param localImage LocalImage
 * @param directory 이미지가 저장된 최상위 디렉토리 이름
 * @see LocalImage
 */
data class LocalImageWithDirectory(
    val localImage: LocalImage,
    val directory: String,
)
