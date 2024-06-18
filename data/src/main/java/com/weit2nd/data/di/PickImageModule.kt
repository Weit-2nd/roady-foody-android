package com.weit2nd.data.di

import com.weit2nd.data.repository.pickimage.PickImageRepositoryImpl
import com.weit2nd.data.source.pickimage.PickImageDataSource
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.repository.pickimage.PickImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PickImageModule {

    @Singleton
    @Provides
    fun providesPickImageRepository(
        dataSource: PickImageDataSource,
    ): PickImageRepository {
        return PickImageRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun providesPickImageDataSource(
        activityProvider: ActivityProvider,
    ): PickImageDataSource {
        return PickImageDataSource(activityProvider)
    }
}
