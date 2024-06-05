package com.weit2nd.data.di

import com.weit2nd.data.util.ActivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Singleton
    @Provides
    fun providesActivityProvider(): ActivityProvider {
        return ActivityProvider()
    }
}
