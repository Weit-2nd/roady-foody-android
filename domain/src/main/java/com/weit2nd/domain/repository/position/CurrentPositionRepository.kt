package com.weit2nd.domain.repository.position

import com.weit2nd.domain.model.Location

interface CurrentPositionRepository {
    suspend fun getCurrentPosition(): Location
}
