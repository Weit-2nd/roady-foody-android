package com.weit2nd.domain.repository.localimage

import com.weit2nd.domain.model.localimage.LocalAlbum
import com.weit2nd.domain.model.localimage.LocalImage

interface LocalImageRepository {
    suspend fun getImages(
        path: String? = null,
        count: Int? = null,
        offset: Int = 0,
    ): List<LocalImage>

    suspend fun getAlbums(): List<LocalAlbum>
}
