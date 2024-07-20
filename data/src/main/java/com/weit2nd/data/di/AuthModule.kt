package com.weit2nd.data.di

import android.content.Context
import com.weit2nd.data.interceptor.AuthAuthenticator
import com.weit2nd.data.interceptor.AuthInterceptor
import com.weit2nd.data.interceptor.LoginInterceptor
import com.weit2nd.data.service.RefreshTokenService
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.util.SecurityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesAuthAuthenticator(dataSource: TokenDataSource): AuthAuthenticator {
        return AuthAuthenticator(dataSource)
    }

    @Singleton
    @Provides
    fun providesTokenDataSource(
        @ApplicationContext context: Context,
        service: RefreshTokenService,
        securityProvider: SecurityProvider,
    ): TokenDataSource {
        return TokenDataSource(
            context = context,
            refreshTokenService = service,
            securityProvider = securityProvider,
        )
    }

    @Singleton
    @Provides
    fun providesTokenService(
        @DefaultNetwork retrofit: Retrofit,
    ): RefreshTokenService {
        return retrofit.create(RefreshTokenService::class.java)
    }

    @Singleton
    @Provides
    fun providesSecurityProvider(): SecurityProvider {
        return SecurityProvider()
    }
}
