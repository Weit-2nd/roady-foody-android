package com.weit2nd.domain.model.localimage

/**
 * @param lastModified 마지막 수정 시간 Millis
 * @param uri 이미지 uri
 */
data class LocalImage(
    val lastModified: Long,
    val uri: String,
)
