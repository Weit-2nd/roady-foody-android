package com.weit2nd.data.di

import com.weit2nd.data.interceptor.AuthInterceptor
import com.weit2nd.data.source.auth.AuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    fun providesAuthInterceptor(
        dataSource: AuthDataSource,
    ): AuthInterceptor {
        return AuthInterceptor(dataSource)
    }

    @Singleton
    @Provides
    fun providesAuthDataSource(): AuthDataSource {
        return AuthDataSource()
    }
}
