package com.weit2nd.data.di

import com.weit2nd.data.repository.test.TestRepositoryImpl
import com.weit2nd.data.service.TestService
import com.weit2nd.data.source.test.TestDataSource
import com.weit2nd.domain.repository.test.TestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Singleton
    @Provides
    fun providesTestRepository(
        testDataSource: TestDataSource,
    ): TestRepository {
        return TestRepositoryImpl(
            testDataSource = testDataSource,
        )
    }

    @Singleton
    @Provides
    fun providesTestDataSource(
        testService: TestService,
    ): TestDataSource {
        return TestDataSource(
            service = testService,
        )
    }

    @Singleton
    @Provides
    fun providesTestService(
        @DefaultNetwork retrofit: Retrofit,
    ): TestService {
        return retrofit.create(TestService::class.java)
    }
}
