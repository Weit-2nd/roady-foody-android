package com.weit2nd.domain.repository.test

interface TestRepository {
    suspend fun getSuccess(name: String): String
}
