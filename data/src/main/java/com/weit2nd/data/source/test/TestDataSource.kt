package com.weit2nd.data.source.test

import com.weit2nd.data.service.TestService
import javax.inject.Inject

class TestDataSource @Inject constructor(
    private val service: TestService,
) {
    suspend fun getSuccess(
        name: String
    ): String = service.getSuccess(
        name = name,
    )
}
