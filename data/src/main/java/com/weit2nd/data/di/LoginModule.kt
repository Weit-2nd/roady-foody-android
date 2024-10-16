package com.weit2nd.data.di

import android.content.Context
import com.weit2nd.data.repository.login.LoginRepositoryImpl
import com.weit2nd.data.service.LoginService
import com.weit2nd.data.source.login.LoginDataSource
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.repository.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Singleton
    @Provides
    fun providesLoginRepository(
        @ApplicationContext context: Context,
        loginDataSource: LoginDataSource,
        tokenDataSource: TokenDataSource,
        activityProvider: ActivityProvider,
    ): LoginRepository {
        return LoginRepositoryImpl(
            context = context,
            loginDataSource = loginDataSource,
            tokenDataSource = tokenDataSource,
            activityProvider = activityProvider,
        )
    }

    @Singleton
    @Provides
    fun providesLoginDataSource(service: LoginService): LoginDataSource {
        return LoginDataSource(service)
    }

    @Provides
    @Singleton
    fun providesLoginService(
        @LoginNetwork retrofit: Retrofit,
    ): LoginService {
        return retrofit.create(LoginService::class.java)
    }
}
