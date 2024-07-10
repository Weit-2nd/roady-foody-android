package com.weit2nd.data.repository.pickimage

import com.weit2nd.data.source.pickimage.PickImageDataSource
import com.weit2nd.domain.repository.pickimage.PickImageRepository
import javax.inject.Inject

class PickImageRepositoryImpl @Inject constructor(
    private val dataSource: PickImageDataSource,
) : PickImageRepository {
    override suspend fun pickImage(): String? {
        return dataSource.pickImage()?.toString()
    }

    override suspend fun pickImages(maximumSelect: Int): List<String> {
        return dataSource.pickImages(maximumSelect).map { it.toString() }
    }
}
