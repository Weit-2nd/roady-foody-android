package com.weit2nd.domain.model.localimage

/**
 * @param path 앨범의 절대 경로
 * @param thumbnailUri 대표 이미지 uri
 * @param count 총 이미지 개수
 */
data class LocalAlbum(
    val path: String,
    val thumbnailUri: String,
    val count: Int,
)
