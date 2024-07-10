package com.weit2nd.data.repository.localimage

import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.domain.model.localimage.LocalAlbum
import com.weit2nd.domain.model.localimage.LocalImage
import com.weit2nd.domain.repository.localimage.LocalImageRepository
import javax.inject.Inject

class LocalImageRepositoryImpl @Inject constructor(
    private val localImageDatasource: LocalImageDatasource,
) : LocalImageRepository {
    override suspend fun getImages(
        path: String?,
        count: Int?,
        offset: Int,
    ): List<LocalImage> {
        return localImageDatasource.getImages(path, count, offset).map { it.localImage }
    }

    override suspend fun getAlbums(): List<LocalAlbum> {
        return localImageDatasource
            .getImages()
            .groupBy { it.directory }
            .map { (directory, localImages) ->
                LocalAlbum(
                    path = directory,
                    thumbnailUri = localImages.first().localImage.uri,
                    count = localImages.size,
                )
            }
    }
}
