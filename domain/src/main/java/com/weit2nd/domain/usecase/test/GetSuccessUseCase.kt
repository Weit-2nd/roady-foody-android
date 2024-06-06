package com.weit2nd.domain.usecase.test

import com.weit2nd.domain.repository.test.TestRepository
import javax.inject.Inject

class GetSuccessUseCase @Inject constructor(
    private val repository: TestRepository,
) {
    suspend operator fun invoke(name: String): String =
        repository.getSuccess(
            name = name,
        )
}
