package com.weit2nd.domain.usecase.position

import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.position.CurrentPositionRepository
import javax.inject.Inject

class GetCurrentPositionUseCase @Inject constructor(
    private val repository: CurrentPositionRepository,
) {
    suspend operator fun invoke(): Location {
        return repository.getCurrentPosition()
    }
}
