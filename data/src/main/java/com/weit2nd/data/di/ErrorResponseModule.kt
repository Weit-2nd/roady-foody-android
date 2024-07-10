package com.weit2nd.data.di

import com.squareup.moshi.Moshi
import com.weit2nd.data.interceptor.ErrorResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorResponseModule {
    @Singleton
    @Provides
    fun providesErrorResponseInterceptor(moshi: Moshi): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(moshi)
    }
}
