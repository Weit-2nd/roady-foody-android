package com.weit2nd.data.di

import com.weit2nd.data.interceptor.AuthInterceptor
import com.weit2nd.data.interceptor.LoginInterceptor
import com.weit2nd.data.source.token.TokenDataSource
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
    fun providesLoginInterceptor(dataSource: TokenDataSource): LoginInterceptor {
        return LoginInterceptor(dataSource)
    }

    @Singleton
    @Provides
    fun providesAuthInterceptor(dataSource: TokenDataSource): AuthInterceptor {
        return AuthInterceptor(dataSource)
    }

    @Singleton
    @Provides
    fun providesTokenDataSource(): TokenDataSource {
        return TokenDataSource()
    }
}
