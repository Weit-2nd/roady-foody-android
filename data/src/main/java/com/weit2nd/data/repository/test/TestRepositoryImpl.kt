package com.weit2nd.data.repository.test

import com.weit2nd.data.source.test.TestDataSource
import com.weit2nd.domain.repository.test.TestRepository
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource,
) : TestRepository {
    override suspend fun getSuccess(name: String): String =
        testDataSource.getSuccess(
            name = name,
        )
}
