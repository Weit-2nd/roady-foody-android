package com.weit2nd.domain.repository.position

import com.weit2nd.domain.model.Coordinate

interface CurrentPositionRepository {
    suspend fun getCurrentPosition(): Coordinate
}
