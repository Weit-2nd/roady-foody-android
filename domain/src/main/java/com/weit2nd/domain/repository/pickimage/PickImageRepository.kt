package com.weit2nd.domain.repository.pickimage

interface PickImageRepository {
    suspend fun pickImage(): String?

    suspend fun pickImages(
        maximumSelect: Int,
    ): List<String>
}
