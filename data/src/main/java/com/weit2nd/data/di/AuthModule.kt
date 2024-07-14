package com.weit2nd.data.di

import com.weit2nd.data.interceptor.AuthInterceptor
import com.weit2nd.data.interceptor.LoginInterceptor
import com.weit2nd.data.service.RefreshTokenService
import com.weit2nd.data.source.token.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    fun providesTokenDataSource(service: RefreshTokenService): TokenDataSource {
        return TokenDataSource(service)
    }

    @Singleton
    @Provides
    fun providesTokenService(
        @DefaultNetwork retrofit: Retrofit,
    ): RefreshTokenService {
        return retrofit.create(RefreshTokenService::class.java)
    }
}
